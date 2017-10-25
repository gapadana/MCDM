package policyPackage;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import policyPackage.emissionFactors.EmissionFactor;
import policyPackage.emissionFactors.EmissionFactorHandler;
import policyPackage.resources.Equipment;
import policyPackage.resources.Material;
import policyPackage.resources.ResourceHandler;
import policyPackage.suppliers.Supplier;
import policyPackage.suppliers.SupplierHandler;
import structurePackage.Alternative;
import structurePackage.Element;


public class ExcelImporter {

    public static boolean importExcel(String address, LinkedHashMap<String, Element> elements) {

        try {
            FileInputStream excelFile = new FileInputStream(new File(address));
            Workbook workbook = new XSSFWorkbook(excelFile);
            int numberOfSheets = workbook.getNumberOfSheets();
            System.out.println("numberOfSheets = " + numberOfSheets);

            getCriteria(elements, workbook);
            getSuppliers(workbook);
            getResources(elements, workbook);
            getEquipments(elements, workbook);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void getEquipments(LinkedHashMap<String, Element> elements, Workbook workbook) {
        Sheet dataTypeSheet = workbook.getSheetAt(3);
        Iterator<Row> iterator = dataTypeSheet.iterator();
        LinkedHashMap<Integer, String> resourceCodes = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> resourceNames = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> energySources = new LinkedHashMap<>();
        LinkedHashMap<Integer, Double> powers = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> units = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> types = new LinkedHashMap<>();

        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            String elementName = null;
            String alternativeName = null;
            Element tempElement = null;

            EmissionFactor ef = null;
            boolean namesRow = false;
            boolean codesRow = false;
            boolean sourceRow = false;
            boolean powerRow = false;
            boolean unitRow = false;
            boolean efRow = false;
            boolean typesRow = false;

            int x = 0;
            while (cellIterator.hasNext()) {
                x++;
                if(x==1){
                    System.out.println("boz");
                }
                Cell currentCell = cellIterator.next();
                System.out.print(currentCell.getCellTypeEnum());
                CellRangeAddress mergedRegionForCell = getMergedRegionForCell(currentCell);
                if (mergedRegionForCell != null) {
                    int firstColumn = mergedRegionForCell.getFirstColumn();
                    int firstRow = mergedRegionForCell.getFirstRow();
                    currentCell = dataTypeSheet.getRow(firstRow).getCell(firstColumn);
                }
                if (currentCell.getCellTypeEnum() == CellType.STRING) {
                    String cellValue = currentCell.getStringCellValue().trim();
                    System.out.print("[" + cellValue + "]\t");
                    switch (cellValue.toLowerCase()) {
                        case "equipment and tools (eqp)":
                            break;
                        case "resource name":
                            namesRow = true;
                            break;
                        case "resource code":
                            codesRow = true;
                            break;
                        case "energy source":
                            sourceRow = true;
                            break;
                        case "building elements":
                            typesRow = true;
                            break;
                        case "alternatives":
                            typesRow = true;
                            break;
                        case "power":
                            powerRow = true;
                            break;
                        case "unit":
                            unitRow = true;
                            break;
                        case "ef":
                            efRow = true;
                            break;
                        default:
                            if (namesRow)
                                resourceNames.put(x, cellValue);
                            else if (codesRow)
                                resourceCodes.put(x, cellValue);
                            else if (sourceRow)
                                energySources.put(x, cellValue);
                            else if (unitRow)
                                units.put(x, cellValue);
                            else if (efRow)
                                ef = new EmissionFactor(cellValue);
                            else if (typesRow)
                                types.put(x, cellValue);
                            else if (!powerRow) {
                                if (x == 1) {
                                    elementName = cellValue;
                                    if (elements.containsKey(elementName)) {
                                        tempElement = elements.get(elementName);
                                    }
                                } else if (x == 2) {
                                    if (tempElement != null)
                                        alternativeName = cellValue;
                                }
                            }
                            break;
                    }
                } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                    double cellValue = currentCell.getNumericCellValue();
                    System.out.print(cellValue + "\t");
                    if (powerRow) {
                        powers.put(x, cellValue);
                    } else if (efRow && ef != null) {
                        System.out.println(x + " cellValue-> " + types.get(x));
                        setEmissionFactor(cellValue, types.get(x), ef, resourceCodes.get(x));
                    } else if (alternativeName != null) {
                        Element element = elements.get(elementName);
                        if (element.alternatives.containsKey(alternativeName)) {
                            Alternative alternative = element.alternatives.get(alternativeName);
                            if (types.get(x).equalsIgnoreCase("ave.")) {
                                alternative.equipments.put(resourceCodes.get(x),
                                        new Equipment(
                                                resourceNames.get(x),
                                                resourceCodes.get(x),
                                                energySources.get(x),
                                                powers.get(x),
                                                units.get(x)));
                                Equipment equipment = alternative.equipments.get(resourceCodes.get(x));
                                equipment.setAvg(cellValue);
                            } else if (types.get(x).equalsIgnoreCase("std")) {
                                Equipment equipment = alternative.equipments.get(resourceCodes.get(x));
                                equipment.setStd(cellValue);
                            } else if (types.get(x).equalsIgnoreCase("min")) {
                                Equipment equipment = alternative.equipments.get(resourceCodes.get(x));
                                equipment.setMin(cellValue);
                            } else if (types.get(x).equalsIgnoreCase("max")) {
                                Equipment equipment = alternative.equipments.get(resourceCodes.get(x));
                                equipment.setMax(cellValue);
                                ResourceHandler.i().addEQP(equipment);
                            }
                        }
                    }
                }
            }
            System.out.println();
        }
    }

    private static void setEmissionFactor(double cellValue, String type, EmissionFactor ef, String code) {
        System.out.println("cellValue = " + cellValue);
        System.out.println("type = " + type);
        System.out.println("ef = " + ef);
        System.out.println("code = " + code);
        switch (type.toLowerCase()) {
            case "ave.":
                ef.setAvg(cellValue);
                break;
            case "min":
                ef.setMin(cellValue);
                break;
            case "max":
                ef.setMax(cellValue);
                EmissionFactorHandler.i().addEmissionFactor(code, ef);
                break;
        }
    }

    private static void getSuppliers(Workbook workbook) {
        Sheet dataTypeSheet = workbook.getSheetAt(1);
        Iterator<Row> iterator = dataTypeSheet.iterator();
        LinkedHashMap<Integer, String> titles = new LinkedHashMap<>();
        String efUnit = "";

        int y = 0;

        while (iterator.hasNext()) {
            y++;
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            Supplier supplier = null;

            int x = 0;
            while (cellIterator.hasNext()) {
                x++;

                Cell currentCell = cellIterator.next();

                CellRangeAddress mergedRegionForCell = getMergedRegionForCell(currentCell);
                if (mergedRegionForCell != null) {
                    int firstColumn = mergedRegionForCell.getFirstColumn();
                    int firstRow = mergedRegionForCell.getFirstRow();
                    currentCell = dataTypeSheet.getRow(firstRow).getCell(firstColumn);
                }
                if (currentCell.getCellTypeEnum() == CellType.STRING) {
                    System.out.print(currentCell.getStringCellValue() + "\t");
                    if (y == 1) {
                        efUnit = currentCell.getStringCellValue();
                    } else if (y == 2) {
                        String cellValue = currentCell.getStringCellValue();
                        titles.put(x, cellValue);
                    } else if (x == 1) {
                        String cellValue = currentCell.getStringCellValue().trim();
                        supplier = new Supplier(cellValue);
                    } else {
                        String cellValue = currentCell.getStringCellValue().trim();
                        if (supplier == null)
                            break;
                        switch (titles.get(x).toLowerCase()) {
                            case "supplier":
                                supplier.setSupplier(cellValue);
                                break;
                            case "name":
                                supplier.setName(cellValue);
                                break;
                            case "unit":
                                supplier.setUnit(cellValue);
                                break;
                            case "site":
                                supplier.setSite(cellValue);
                                break;
                        }
                    }
                } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                    double cellValue = currentCell.getNumericCellValue();
                    System.out.print(cellValue + "\t");
                    if (supplier == null)
                        break;
                    switch (titles.get(x).toLowerCase()) {
                        case "distance-km":
                            supplier.setDistance(cellValue);
                            break;
                        case "capacity":
                            supplier.setCapacity(cellValue);
                            break;
                        case "power":
                            supplier.setPower(cellValue);
                            break;
                        case "ave.":
                            supplier.setAve(cellValue);
                            break;
                        case "min":
                            supplier.setMin(cellValue);
                            break;
                        case "max":
                            supplier.setMax(cellValue);
                            break;
                    }
                }
                if (supplier != null) {
                    supplier.setEFUnit(efUnit);
                    SupplierHandler.i().addSupplier(supplier);
                }
            }
        }
    }

    private static void getResources(LinkedHashMap<String, Element> elements, Workbook workbook) {
        Sheet datatypeSheet = workbook.getSheetAt(2);
        Iterator<Row> iterator = datatypeSheet.iterator();
        LinkedHashMap<Integer, String> resourceCodes = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> resourceUnits = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> resourceNames = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> descriptions = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> types = new LinkedHashMap<>();

        int y = 0;

        while (iterator.hasNext()) {
            y++;
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            String elementName = null;
            String alternativeName = null;
            Element tempElement = null;

            EmissionFactor ef = null;
            boolean namesRow = false;
            boolean codesRow = false;
            boolean descriptionsRow = false;
            boolean unitRow = false;
            boolean efRow = false;
            boolean typesRow = false;

            int x = 0;
            while (cellIterator.hasNext()) {
                x++;

                Cell currentCell = cellIterator.next();

                CellRangeAddress mergedRegionForCell = getMergedRegionForCell(currentCell);
                if (mergedRegionForCell != null) {
                    int firstColumn = mergedRegionForCell.getFirstColumn();
                    int firstRow = mergedRegionForCell.getFirstRow();
                    currentCell = datatypeSheet.getRow(firstRow).getCell(firstColumn);
                }
                if (currentCell.getCellTypeEnum() == CellType.STRING) {
                    String cellValue = currentCell.getStringCellValue().trim();
                    switch (cellValue.toLowerCase()) {
                        case "material (mtl)":
                            break;
                        case "resource code":
                            codesRow = true;
                            break;
                        case "resource name":
                            namesRow = true;
                            break;
                        case "unit":
                            unitRow = true;
                            break;
                        case "description":
                            descriptionsRow = true;
                            break;
                        case "ef":
                            efRow = true;
                            break;
                        case "building elements":
                            typesRow = true;
                            break;
                        case "alternatives":
                            typesRow = true;
                            break;
                        default:
                            if (codesRow)
                                resourceCodes.put(x, cellValue);
                            else if (namesRow)
                                resourceNames.put(x, cellValue);
                            else if (unitRow)
                                resourceUnits.put(x, cellValue);
                            else if (descriptionsRow)
                                descriptions.put(x, cellValue);
                            else if (typesRow)
                                types.put(x, cellValue);
                            else if (efRow)
                                ef = new EmissionFactor(cellValue);
                            else {
                                if (x == 1) {
                                    elementName = cellValue;
                                    if (elements.containsKey(elementName))
                                        tempElement = elements.get(elementName);
                                } else if (x == 2) {
                                    if (tempElement != null)
                                        alternativeName = cellValue;
                                }
                            }
                            break;
                    }
                } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                    double cellValue = currentCell.getNumericCellValue();
                    System.out.print(cellValue + "\t");
                    if (efRow && ef != null) {
                        setEmissionFactor(cellValue, types.get(x), ef, resourceCodes.get(x));
                    }else if (alternativeName != null) {
                        if (elements.containsKey(elementName)) {
                            Element element = elements.get(elementName);
                            if (element.alternatives.containsKey(alternativeName)) {
                                Alternative alternative = element.alternatives.get(alternativeName);
                                if (types.get(x).equalsIgnoreCase("ave.")) {
                                    alternative.materials.put(resourceCodes.get(x),
                                            new Material(
                                                    resourceNames.get(x),
                                                    descriptions.get(x),
                                                    resourceCodes.get(x),
                                                    resourceUnits.get(x)));
                                    Material material = alternative.materials.get(resourceCodes.get(x));
                                    material.setAvg(cellValue);
                                } else if (types.get(x).equalsIgnoreCase("std")) {
                                    Material material = alternative.materials.get(resourceCodes.get(x));
                                    material.setStd(cellValue);
                                } else if (types.get(x).equalsIgnoreCase("min")) {
                                    Material material = alternative.materials.get(resourceCodes.get(x));
                                    material.setMin(cellValue);
                                } else if (types.get(x).equalsIgnoreCase("max")) {
                                    Material material = alternative.materials.get(resourceCodes.get(x));
                                    material.setMax(cellValue);
                                    ResourceHandler.i().addMTL(material);
                                }
                            }
                        }
                    }
                }
            }
            System.out.println();
        }
        System.out.println("boz");
    }

    private static void getCriteria(LinkedHashMap<String, Element> elements, Workbook workbook) {
        Sheet dataTypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = dataTypeSheet.iterator();
        LinkedHashMap<Integer, Criterion> criteria = new LinkedHashMap<>();


        int y = 0;
//            boolean isImpact = false;
        while (iterator.hasNext()) {
            y++;
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            String elementName;
            Element tempElement = null;
            Alternative tempAlternative = null;

            int x = 0;
            while (cellIterator.hasNext()) {
                x++;

                Cell currentCell = cellIterator.next();

                CellRangeAddress mergedRegionForCell = getMergedRegionForCell(currentCell);
                if (mergedRegionForCell != null) {
                    int firstColumn = mergedRegionForCell.getFirstColumn();
                    int firstRow = mergedRegionForCell.getFirstRow();
                    currentCell = dataTypeSheet.getRow(firstRow).getCell(firstColumn);
                }
                //getCellTypeEnum shown as deprecated for version 3.15
                //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
                if (currentCell.getCellTypeEnum() == CellType.STRING) {
                    System.out.print(currentCell.getStringCellValue() + "\t");
                    if (y == 1) {
                        //ignore title
                    } else if (y == 2) {
                        if (mergedRegionForCell == null) {
                            Criterion criterion = new Criterion();
                            criterion.resourceCode = currentCell.getStringCellValue();
                            criteria.put(x, criterion);
                        }
                    } else if (y == 3) {
                        if (criteria.containsKey(x)) {
                            criteria.get(x).name = currentCell.getStringCellValue();
                        }
                    } else if (x == 1) {
                        elementName = currentCell.getStringCellValue();
                        if (!elements.containsKey(elementName)) {
                            tempElement = new Element(elementName);
                            elements.put(elementName, tempElement);
                        } else {
                            tempElement = elements.get(elementName);
                        }
                    } else if (x == 2) {
                        tempAlternative = new Alternative(currentCell.getStringCellValue());
                    }
                } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                    System.out.print(currentCell.getNumericCellValue() + "\t");
                    double cellValue = currentCell.getNumericCellValue();
                    if (criteria.containsKey(x))
                        if(tempElement != null){
                            if(tempAlternative != null){
                                tempAlternative.addCriterion(criteria.get(x).resourceCode, criteria.get(x).name, cellValue);
                                tempElement.addAlternative(tempAlternative.getAlternativeName(), tempAlternative);
                            }
                        }
                } else {
                    System.out.print(x + "\t");
                }
            }
            System.out.println();
        }
    }

    private static CellRangeAddress getMergedRegionForCell(Cell c) {
        Sheet s = c.getRow().getSheet();
        for (CellRangeAddress mergedRegion : s.getMergedRegions()) {
            if (mergedRegion.isInRange(c.getRowIndex(), c.getColumnIndex())) {
                // This region contains the cell in question
                return mergedRegion;
            }
        }
        // Not in any
        return null;
    }

}

