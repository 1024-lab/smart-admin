package net.lab1024.smartadmin.util.excel;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author zhuoda
 */
public class SmartSheet {
    final String name;

    private final int rowCount;
    private final int columnCount;
    private final String[][] datas;

    public SmartSheet(org.apache.poi.ss.usermodel.Sheet sheet) {
        this.name = sheet.getSheetName();
        this.rowCount = sheet.getLastRowNum() + 1;
        // 初始化基本数据
        int maxColumnCount = 0;
        this.datas = new String[rowCount][];
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            int _columnCount = row.getLastCellNum() + 1;
            this.datas[rowIndex] = new String[_columnCount];
            for (int colIndex = 0; colIndex < _columnCount; colIndex++) {
                this.datas[rowIndex][colIndex] = getCellContents(row.getCell(colIndex));
            }
            if (maxColumnCount < _columnCount) {
                maxColumnCount = _columnCount;
            }
        }
        this.columnCount = maxColumnCount;
        // 根据单元格合并情况,填充内容
        for (int index = 0; index < sheet.getNumMergedRegions(); index++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(index);
            String upperLeftData = this.datas[mergedRegion.getFirstRow()][mergedRegion.getFirstColumn()];
            for (int rowIndex = mergedRegion.getFirstRow(); rowIndex <= mergedRegion.getLastRow(); rowIndex++) {
                String[] _rowDatas = this.datas[rowIndex];
                if (_rowDatas == null) {
                    this.datas[rowIndex] = new String[mergedRegion.getLastColumn() + 1];
                } else if (_rowDatas.length < mergedRegion.getLastColumn() + 1) {
                    String[] newStrArray = new String[mergedRegion.getLastColumn() + 1];
                    System.arraycopy(_rowDatas, 0, newStrArray, 0, _rowDatas.length);
                    this.datas[rowIndex] = newStrArray;
                }
                for (int colIndex = mergedRegion.getFirstColumn(); colIndex <= mergedRegion.getLastColumn(); colIndex++) {
                    this.datas[rowIndex][colIndex] = upperLeftData;
                }
            }
        }
    }

    private String getCellContents(Cell cell) {
        if (cell == null) {
            return null;
        }
        return getCellContents(cell.getCellType(), cell);
    }

    private String getCellContents(CellType type, Cell cell) {
        switch (type) {
            case BLANK:
                return "";
            case NUMERIC:
                return cell.getStringCellValue();
            case STRING:
                return cell.getStringCellValue();
            case FORMULA:
                return getCellContents(cell.getCachedFormulaResultType(), cell);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case ERROR:
            default:
                throw new IllegalArgumentException(String.format("unsupported cell type:%d, col:%d, row:%d, sheet:%s", cell.getCellType(), cell.getColumnIndex(),
                        cell.getRowIndex(), getName()));
        }
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public String getValue(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= datas.length) {
            return "";
        }
        if (columnIndex < 0 || datas[rowIndex] == null || columnIndex >= datas[rowIndex].length) {
            return "";
        }
        String value = datas[rowIndex][columnIndex];
        return value == null ? "": value;
    }


    public String getName() {
        return name;
    }



}

