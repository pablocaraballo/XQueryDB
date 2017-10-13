import java.io.IOException;
import java.util.Scanner;

import javax.xml.xquery.XQException;

import javafx.application.Application;

public class Monillos {

	public static void main(String[] args) throws Exception {
			
			Menu();
		}
		
		public static void Menu() throws Exception{
			GestorDB gestor=new GestorDB();
			Scanner sc = new Scanner(System.in);
			int opcion = 0;
			
			while(opcion != 5){
				System.out.println("##############################################");
				System.out.println("##################  MENU  ####################");
				System.out.println("##############################################");
				
				System.out.println("1.-Las 5 marcas de coches mas accidentadas");
				System.out.println("2.-Top 10 de los barrios con mas accidentes");
				System.out.println("3.-Dia del mes con mas y menos accidentes.");
				System.out.println("4.-Accidentes por año y media de víctimas desde 2010 a 2015.");
				System.out.println("5.-Borrar registro");
				System.out.println("6.-Editar registro");
				System.out.println("7.-Cerrar sesion");
				System.out.println("Introduce la opcion: ");
				opcion =sc.nextInt();
				switch(opcion){
				case 1: gestor.crashCar(); break;
				case 2: gestor.tBarriosAccidente(); break;
				case 3: gestor.diaAccidente(); break;
				case 4: gestor.prueba();break;
				case 5: gestor.deleteReg(); break;
				case 6: gestor.updateReg(); break;
				case 7: gestor.cerrarSesion();  break;
				default: System.out.println("Opcion incorrecta, vuelva a intentarlo"); break;
				}
			}
			
		}

}
