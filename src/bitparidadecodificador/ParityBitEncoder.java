/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitparidadecodificador;

import java.util.Scanner;

/**
 *
 * @author danieljunior
 */
public class ParityBitEncoder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
       /*  
            AUTOMATIZA A CRIAÇÃO DE ARQUIVOS BINÁRIOS
            FileHandler.generateFile("teste.bin", "Isso é um outro teste!"); 
        */
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Digite o nome do arquivo de entrada: ");
        String filename = keyboard.nextLine();
        System.out.print("Digite o nome do arquivo de saída: ");
        String output = keyboard.nextLine();
        System.out.println();

        Encoder e = new Encoder();      
        e.encoder(filename, output);
    }
    
}
