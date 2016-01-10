/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitparidadedecodicador;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.BitSet;

/**
 *
 * @author renan azevedo
 */
public class Decoder {
    
    public static final int READ_BYTES = 10;
    public static final int BYTE = 8;
    public static final int DISPLAY_MASK = 1 << 7;
    public int matrix[][] ;

    public void decoder(String filename, String output) throws Exception {
        
        File source = new File(filename);
        File destiny = new File(output);
        
        try (RandomAccessFile in = new RandomAccessFile(source, "rw"); FileOutputStream out = new FileOutputStream(destiny)) {
          
            long redundant = 0;
            while (in.getFilePointer() < in.length()) {
                byte[] bytes = null;
                long availableBytes = in.length() - in.getFilePointer();
                if (availableBytes > READ_BYTES) {
                    bytes = new byte[READ_BYTES];
                    in.read(bytes);
                } else {
                    if(availableBytes < 3){
                        System.out.println("Execução abortada. Último bloco com menos de 3 bytes.");
                        break;
                    } else {
                        redundant = READ_BYTES - availableBytes;
                        bytes = new byte[READ_BYTES];
                        in.read(bytes);
                    }
                }

                showBytes(bytes, "lidos");
                
                matrix = populateMatrix(bytes);
                System.out.println();
                showBuildedMatrix();

                int [] rowParity = recoversParity(bytes[0], "linhas (lido do arquivo)");
                int [] columnParity = recoversParity(bytes[1], "colunas (lido do arquivo)");
                System.out.println();
                byte[] parity = calculateBitParity(matrix);
                // guardando em um vetor de inteiros para facilitar manipulação
                int [] calculatedParityLines = recoversParity(parity[0], "linhas (calculado)");
                int [] parityCalculatedColumns = recoversParity(parity[1], "colunas (calculado)");
                
                ArrayList<Integer> linesWithErrors = checkerErrors(rowParity, calculatedParityLines);
                ArrayList<Integer> columnsWithErrors = checkerErrors(columnParity, parityCalculatedColumns);
                
                if(validatesCorrection(linesWithErrors, columnsWithErrors)) {
                    System.out.println("Execução abortada. Foi encontrado um erro irrecuperável.");
                    break;
                }
                
                // salva no arquivo de saída
                for (int i = 2; i < bytes.length - redundant; i++) {
                    out.write(bytes[i]);
                }
                
                System.out.println("\n");
                System.out.println("---------------------------------");
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
    
    private void correctsError(int rowWithError, int columnWithError){
        int bitError =  matrix[rowWithError][columnWithError];
        if(bitError == 1){
            matrix[rowWithError][columnWithError] = 0;
        } else {
            matrix[rowWithError][columnWithError] = 1;
        }
    }
    
    private boolean validatesCorrection(ArrayList<Integer> linesWithErrors, ArrayList<Integer> columnsWithErrors){
      System.out.println();
      if(linesWithErrors.isEmpty() && columnsWithErrors.isEmpty()){
          System.out.println("As paridades são iguais. O dado está correto.");    
      } else {
          
          System.out.println("O dado foi danificado.");
          System.out.println("Foi verificado erro(s) na(s) linha(s): " + (!linesWithErrors.isEmpty() ? linesWithErrors.toString() : "Sem erros nas linhas."));
          System.out.println("Foi verificado erro(s) na(s) coluna(s): " + (!columnsWithErrors.isEmpty() ? columnsWithErrors.toString() : "Sem erros nas colunas."));
          
          if((linesWithErrors.size() > 1 || columnsWithErrors.size() > 1)){
              System.out.println("Recuperação impossível. Não se sabe ao certo quais bits foram danificados.");
              return true;
          } else {
              System.out.println("Recuperação realizada. O bit danificado foi identificado e recuperado.");
              // exite um erro na linha e um na coluna.
              correctsError(linesWithErrors.get(0), columnsWithErrors.get(0));
          }
      }
      return false;
    }
    
    public static ArrayList<Integer> checkerErrors(int [] readParity, int [] calculatedParity){
        ArrayList<Integer> list= new ArrayList<>();
        for (int i = 0; i < readParity.length; i++) {
            if(readParity[i] != calculatedParity[i]){
                list.add(i);
            }
        }
        return list;
    }
    
    public static void display(int value) {
        // para cada bit exibe 0 ou 1
        for (int bit = 1; bit <= 8; bit++) {
            // utiliza displayMask para isolar o bit
            System.out.print((value & DISPLAY_MASK) == 0 ? '0' : '1');
            value <<= 1; // desloca o valor uma posição para a esquerda
            if (bit % 8 == 0) {
                System.out.print(" ");
            } // exibe espaço a cada 8 bits
        } // fim do for
    } // fim do método display
     
    private int [] recoversParity(byte value, String txt){
        int[] rowParity = new int[8];
        // mostrar ou não o print
        if(!txt.isEmpty()){
            System.out.print("Paridade das "+txt+" : ");
        }
        for (int bit = 1; bit <= 8; bit++) {
            System.out.print((value & DISPLAY_MASK) == 0 ? '0' : '1');
            rowParity[bit - 1] = (value & DISPLAY_MASK) == 0 ? 0 : 1;
            value <<= 1;
        } // fim do for
        System.out.println();
        return rowParity;
     }

    private int[][] populateMatrix(byte[] bytes) {
        int matrix[][] = new int[bytes.length - 2][8];
        for (int i = 2; i < bytes.length; i++) {
            int aux = bytes[i];
            for (int bit = 1; bit <= 8; bit++) {
                if ((aux & DISPLAY_MASK) != 0) {
                    matrix[i - 2][bit - 1] = 1;
                }
                aux <<= 1;
            }
        }
        return matrix;
    }

    private void printMatrix() {
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

    private void showBuildedMatrix() {
        System.out.println("Mostrando mapeamento para matriz:\n");
        printMatrix();
        System.out.println("\n");
    }
       
}
