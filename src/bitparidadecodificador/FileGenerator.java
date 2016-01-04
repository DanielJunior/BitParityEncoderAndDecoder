/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitparidadecodificador;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author danieljunior
 */
public class FileGenerator {

    public static void generateFile(String filename, Object data) {
        try (PrintWriter print = new PrintWriter(filename)) {
            print.print(data);
            print.flush();
        } catch (Exception e) {
            System.err.println("Erro na criação do arquivo : " + filename);
        }
    }

}
