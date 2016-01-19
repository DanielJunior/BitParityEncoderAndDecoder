/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package execute;

import bitparidadecodificador.Encoder;
import bitparidadedecodicador.Decoder;
import java.util.Scanner;

/**
 *
 * @author renan azevedo
 */
public class main {
    
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        int keyboard = 0;
        do {
            
            menu();
            System.out.print("Digite aqui: ");
            keyboard = in.nextInt();
            
            switch(keyboard){
                case 1:
                    String [] filenamesEncoder = submenu(in);
                    Encoder e = new Encoder();      
                    e.encoder(filenamesEncoder[0], filenamesEncoder[1]);
                    break;
                case 2:
                    String [] filenamesDecoder = submenu(in);
                    Decoder d = new Decoder();
                    d.decoder(filenamesDecoder[0], filenamesDecoder[1]);
                    break;
                case 3:
                    System.out.println("Fim da execução.");
                    break;
            }
            
        } while(keyboard != 3);
    }
    
    public static void menu(){
        System.out.println("===============MENU===============\n\n"
                        + "Digite (1) para executar o codificador.\n"
                        + "Digite (2) para executar o decodificador.\n"
                        + "Digite (3) para sair.\n\n"
                        +"==================================\n"); 
    }
    
    public static String [] submenu(Scanner in){
        System.out.println("Digite o caminho do arquivo de entrada: ");
        String filename = in.next();
        System.out.println("Digite o caminho do arquivo de saída: ");
        String output = in.next();
        System.out.println();    
        
        String [] names = {filename, output};
        return names;
    }
    
}
