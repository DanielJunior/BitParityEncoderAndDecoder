/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitparidadecodificador;

import java.io.FileInputStream;
import java.io.PrintWriter;

/**
 *
 * @author danieljunior
 */
public class FileHandler {

    public static void generateFile(String filename, Object data) {
        try (PrintWriter print = new PrintWriter(filename)) {
            print.print(data);
            print.flush();
        } catch (Exception e) {
            System.err.println("Erro na criação do arquivo : " + filename);
        }
    }

    public static void readBitFile(String filename) {
        try (FileInputStream in = new FileInputStream(filename)) {
            byte bytes[] = new byte[Encoder.BYTE];
        } catch (Exception e) {
        }
    }

}
