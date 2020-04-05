import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class Main {

    private static int frameWidth = 600;
    private static int frameHeight = 400;


    /**
     * Starts python process, executing script, which open csv file with pandas and prints its content
     *
     * @param interpreterPath path to python interpreter
     * @param csvPath         path to csv file to open
     * @return reader from python process input stream
     */
    private static BufferedReader readFileWithPandas(String interpreterPath, String csvPath) throws IOException {
        File scriptFile = new File("src/main/resources/script.py");
        Process process = Runtime.getRuntime().exec(interpreterPath + " " + scriptFile.getAbsolutePath() + " " + csvPath);
        return new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    /**
     * Reads and returns data from reader
     *
     * @param reader reader, reading table data
     * @return data, in TableData form
     */
    private static TableData receiveData(BufferedReader reader) throws IOException {
        String[] columns = reader.readLine().split(",");
        int columnsNumber = columns.length;
        int rowsNumber = Integer.parseInt(reader.readLine());
        String[][] data = new String[rowsNumber][columnsNumber];
        for (int i = 0; i < rowsNumber; i++) {
            String[] values = reader.readLine().split(",");
            data[i] = values;
        }
        return new TableData(columns, data);
    }

    /**
     * Creates JFrame from table data
     *
     * @param tableData table content
     * @return frame with it
     */
    private static JFrame createTable(TableData tableData) {
        JFrame frame = new JFrame();
        JTable jt = new JTable(tableData.getData(), tableData.getColumns());
        JScrollPane sp = new JScrollPane(jt);
        frame.add(sp);
        frame.setSize(frameWidth, frameHeight);
        return frame;
    }

    /**
     * Entry point of application
     */
    public static void main(String[] args) throws IOException {
        String interpreterPath = args[0];
        String csvPath = args[1];
        JFrame frame = createTable(receiveData(readFileWithPandas(interpreterPath, csvPath)));
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    /**
     * Class, containing table columns names and data
     */
    private static class TableData {
        private String[] columns;
        private String[][] data;

        TableData(String[] columns, String[][] data) {
            this.columns = columns;
            this.data = data;
        }

        String[] getColumns() {
            return columns;
        }

        String[][] getData() {
            return data;
        }
    }
}