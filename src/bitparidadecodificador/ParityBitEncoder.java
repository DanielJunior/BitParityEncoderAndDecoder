/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitparidadecodificador;

/**
 *
 * @author danieljunior
 */
public class ParityBitEncoder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        FileGenerator.generateFile("teste.bin", "Isso é um outro teste!");
        Encoder e = new Encoder();
        e.encoder("teste.bin");
    }
    
}
