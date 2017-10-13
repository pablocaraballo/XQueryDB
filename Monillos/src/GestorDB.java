import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.plaf.synth.SynthSeparatorUI;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQResultSequence;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.xqj.exist.ExistXQDataSource;

public class GestorDB extends Application {
	
	static ArrayList<String>marcas=new ArrayList<String>();
	public ArrayList<String>barrios=new ArrayList<String>();
	public ArrayList<String>dias=new ArrayList<String>();
	public ArrayList<String>datos=new ArrayList<String>();
	public int totalVictimas=0;
	public XQConnection conn;
	public ObservableList<PieChart.Data> pieChartData;
	BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
	
	public GestorDB() throws XQException{
		XQDataSource xqs=new ExistXQDataSource();
		xqs.setProperty("serverName", "15.83.18.66");
		xqs.setProperty("port", "8080");
		xqs.setProperty("user","admin");
		xqs.setProperty("password", "admin");
		conn=xqs.getConnection();
	}
	
	public void cerrarSesion() throws XQException{
		conn.close();
	}
	
	//INTRODUCE EL AÑO Y MUESTRA LAS MARCAS DE COCHES Y LOS ACCIDENTES DE ESTAS MARCAS
	public void crashCar() throws XQException, IOException{
		
		
		System.out.println("Introduce el año: ");
		String any=br.readLine();
		
		System.out.println("La operación tardará aproximadamente 10 segundos. Espere por favor...");
		
		XQExpression xqe=conn.createExpression();
		
		String query="(for $a in distinct-values(doc('/TrabajoXML/Vehiculos/Accidentes_Vehiculos"+any+".xml')//Registre/Descripciomarca) \n" 
					+"let $count:= count(doc('/TrabajoXML/Vehiculos/Accidentes_Vehiculos"+any+".xml')//Registre[Descripciomarca eq $a]) \n" 
					+"order by $count descending \n" 
					+"return concat ($a,'-',$count))[position() <6]";
		XQResultSequence xqrs= xqe.executeQuery(query);
		while(xqrs.next()){
			String marca=xqrs.getItemAsString(null);
			marcas.add(marca);
		}
		
		//MOSTRAMOS TODAS LAS MARCAS
		System.out.println("#######################################################");
		System.out.println("LAS 5 MARCAS DE COCHES  MÁS ACCIDENTADOS EN EL AÑO "+any);
		System.out.println("#######################################################");
		
		for(int i=0; i<marcas.size(); i++){
			System.out.println((i+1)+"-"+marcas.get(i));
				
		}
		
		Application.launch();
	
	}
	
	//BARRIOS DE BARCELONA CON MAS ACCIDENTES
	public void tBarriosAccidente() throws Exception{
		System.out.println("Introduce el año: ");
		String any=br.readLine();
		
		XQExpression xqe=conn.createExpression();
		String query="(for $a in distinct-values(doc('/TrabajoXML/Accidentes/Accidentes"+any+".xml')//Registre/Nombarri) \n"
					+"let $count:= count(doc('/TrabajoXML/Accidentes/Accidentes"+any+".xml')//Registre[Nombarri eq $a]) \n"
					+"order by $count descending \n" 
					+"return concat ($a,': ',$count))[position() <11]";
		
		XQResultSequence xqrs= xqe.executeQuery(query);
		
		while(xqrs.next()){
			String barrio=xqrs.getItemAsString(null);
			barrios.add(barrio);
			
		}
		
		System.out.println("##################################");
		System.out.println("BARRIOS ACCIDENTADOS EN EL AÑO "+any);
		System.out.println("###################################");
		for(String nBarrio : barrios){
			System.out.println(nBarrio);
		}
			
	}
		
		public void diaAccidente() throws Exception{
			System.out.println("Introduce el mes: ");
			String mes=br.readLine();
			
			XQExpression xqe=conn.createExpression();
			String query="for $a in distinct-values(//Registre/Descripciodiasetmana) \n"
						+"let $count:= count(//Registre[Descripciodiasetmana eq $a]) \n"
						+"where //Registre/Nommes = '"+mes+"' \n"
						+"order by $count descending \n" 
						+"return concat ($a,': ',$count)";
			
			XQResultSequence xqrs= xqe.executeQuery(query);
			
			while(xqrs.next()){
				String dia=xqrs.getItemAsString(null);
				dias.add(dia);
				
			}
			
			System.out.println("##################################");
			System.out.println("DIAS MAS Y MENOS ACCIDENTADOS ");
			System.out.println("###################################");
			System.out.println(dias.get(0));
			System.out.println(dias.get(6));
		}

	public void prueba() throws XQException{
		
		XQExpression xqe=conn.createExpression();
		String query="for $a in distinct-values(//Registre/NKAny) \n"
					+"let $any:=$a "
					+"let $registros:=count(//Registre[NKAny=$a]) "
					+"let $victimas:=sum(//Registre/Númerodevíctimes) "
					+"return concat($any,'-',$registros,'-',$victimas)";
		
		XQResultSequence xqrs= xqe.executeQuery(query);
		
		while(xqrs.next()){
			String nums=xqrs.getItemAsString(null);
			StringTokenizer token=new StringTokenizer(nums,"-");
			
			datos.add(token.nextToken()+"-"+token.nextToken());
			totalVictimas=Integer.parseInt(token.nextToken());
		}
		
		totalVictimas=totalVictimas/5;
		
		System.out.println("####################################################################");
		System.out.println("ACCIDENTES POR AÑO Y MEDIA DE VICTIMAS DESDE 2010 A 2015");
		System.out.println("#####################################################################");
		for(String s: datos){
			System.out.println(s);
		}
		System.out.println("Media de vícitmas entre 2010 y 2015: "+totalVictimas);
		
	}
	

	public void deleteReg() throws IOException, XQException{
		
		System.out.println("##########BORRA REGISTRO##########");
		
		System.out.println("Introduce el numero de expediente:");
		String code=br.readLine();
		
		XQExpression xqe=conn.createExpression();
		String delete="update delete doc('/db/TrabajoXML/Accidentes/Accidentes2010.xml')/Registre[Númerodexpedient='"+code+"']";
		xqe.executeCommand(delete);
		System.out.println("El Registro "+code+" ha sido eliminado de la base de datos");
	}
	
	public void updateReg() throws IOException, XQException{
		
		System.out.println("##########EDITA EL REGISTRO##########");
		System.out.println("Introduce el cÃ³digo del registro que quieres reemplazar:  ");
		String code=br.readLine();
		System.out.println("Introduce el nuevo cÃ³digo del registro: ");
		String newcode=br.readLine();
		XQExpression xqe=conn.createExpression();
		String update="update value doc('/db/TrabajoXML/Accidentes/Accidentes2010.xml')/Registre[Númerodexpedient='"+code+"']"
				+"with '"+newcode+"'";
		
		xqe.executeCommand(update);
		System.out.println("El registro con cÃ³digo "+code+" ha sido editado por el registro con codigo: "+newcode);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(new Group());
			primaryStage.setTitle("Marcas de coches accidentadas por año");
			primaryStage.setWidth(500);
			primaryStage.setHeight(500);
			
			pieChartData =  FXCollections.observableArrayList();
			
			for(int i=0; i<marcas.size(); i++){
				String[] split=marcas.get(i).split("-");
				pieChartData.add(new PieChart.Data(split[0], Double.parseDouble(split[1])));
			}
			 
			 final PieChart chart=new PieChart(pieChartData);
			 chart.setTitle("Coches accidentatos");
			
			 ((Group) scene.getRoot()).getChildren().addAll(chart);
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
