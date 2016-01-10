/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitparidadecodificador;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.BitSet;

/**
 *
 * @author danieljunior
 */
public class Encoder {

    public static final int ROW = 0;
    public static final int COLUMN = 1;
    public static final int BYTE = 8;

    public void encoder(String filename, String output) throws Exception {
        //Utilizo esses dois Files para poder fazer a troca de nomes depois
        File source = new File(filename);
        File destiny = new File(output);
        try (RandomAccessFile in = new RandomAccessFile(source, "rw"); FileOutputStream out = new FileOutputStream(destiny)) {
            //variável que monitora se na leitura do array de bytes foi gerado algum byte de redundância
            long redundant = 0;
            while (in.getFilePointer() < in.length()) {
                byte[] bytes = null;
                long availableBytes = in.length() - in.getFilePointer();
                if (availableBytes > BYTE) {
                    bytes = new byte[BYTE];
                    in.read(bytes);
                } else {
                    redundant = BYTE - availableBytes;
                    bytes = new byte[BYTE];
                    in.read(bytes);
                }

                showBytes(bytes, "lidos");

                int matrix[][] = populateMatrix(bytes);

                showBuildedMatrix(matrix);

                byte[] parity = calculateBitParity(matrix);

                showBytes(parity, "de paridade");

                System.out.println("\n");
                System.out.println("---------------------------------");

                out.write(parity[COLUMN]);
                out.write(parity[ROW]);

                for (int i = 0; i < bytes.length - redundant; i++) {
                    out.write(bytes[i]);
                }
            }
            System.out.println("");
            if (redundant > 0) {
                System.out.println("Bytes de redundância: " + redundant);
            }
        } catch (IOException e) {
            System.err.println("Erro na manipulação do arquivo!");
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }

    public static void display(int value) {
        // cria um valor inteiro com 1 no bit mais à esquerda e 0s em outros locais
        int displayMask = 1 << 7;
        // para cada bit exibe 0 ou 1
        for (int bit = 1; bit <= 8; bit++) {
            // utiliza displayMask para isolar o bit
            System.out.print((value & displayMask) == 0 ? '0' : '1');
            value <<= 1; // desloca o valor uma posição para a esquerda
            if (bit % 8 == 0) {
                System.out.print(" ");
            } // exibe espaço a cada 8 bits
        } // fim do for
    } // fim do método display

    private int[][] populateMatrix(byte[] bytes) {
        int displayMask = 1 << 7;
        int matrix[][] = new int[bytes.length][8];
        for (int i = 0; i < bytes.length; i++) {
            int aux = bytes[i];
            for (int bit = 1; bit <= 8; bit++) {
                if ((aux & displayMask) != 0) {
                    matrix[i][bit - 1] = 1;
                }
                aux <<= 1;
            }
        }
        return matrix;
    }

    private void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("");
        }
    }

    private byte[] calculateBitParity(int[][] matrix) {
        BitSet row = new BitSet(BYTE);
        BitSet column = new BitSet(BYTE);
        int rows[] = new int[matrix.length];
        int columns[] = new int[BYTE];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1) {
                    rows[i] += 1;
                    columns[j] += 1;
                }
            }
        }
        for (int i = 0; i < rows.length; i++) {
            int index = rows.length - (i + 1);
            if (rows[i] % 2 != 0) {
                row.set(index);
            }
        }
        for (int i = 0; i < columns.length; i++) {
            int index = rows.length - (i + 1);
            if (columns[i] % 2 != 0) {
                column.set(index);
            }
        }
        byte[] resp = {row.toByteArray()[0], column.toByteArray()[0]};
        return resp;
    }

    private static void showBytes(byte bytes[], String byteType) {
        if (!byteType.isEmpty()) {
            System.out.println("Mostrando bytes " + byteType + " :\n");
        }
        for (int i = 0; i < bytes.length; i++) {
            display(bytes[i]);
            if (i != 0 && (i + 1) % BYTE == 0) {
                System.out.println("");
            }
        }
        System.out.println("\n");
    }

    private void showBuildedMatrix(int[][] matrix) {
        System.out.println("Mostrando mapeamento para matriz:\n");
        printMatrix(matrix);
        System.out.println("\n");
    }
}
