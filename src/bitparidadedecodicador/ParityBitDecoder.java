/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitparidadedecodicador;

import java.util.Scanner;

/**
 *
 * @author renan azevedo
 */
public class ParityBitDecoder {
    
     /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Digite o nome do arquivo de entrada: ");
        String filename = keyboard.nextLine();
        System.out.print("Digite o nome do arquivo de saída: ");
        String output = keyboard.nextLine();
        System.out.println();
        Decoder d = new Decoder();
        d.decoder(filename, output);
    }
    
}
