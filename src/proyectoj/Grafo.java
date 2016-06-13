package proyectoj;


import java.util.*;
import java.io.*;
import static java.lang.Integer.parseInt;



public class Grafo {
    
    int[]  nodos;  // Letras de identificación de nodo 
    public int[][] grafo;  // Matriz de distancias entre nodos
    String  rutaMasCorta;                           // distancia más corta
    int longitudMasCorta = Integer.MAX_VALUE;   // ruta más corta
    List <Nodo>  listos=null; // nodos revisados Dijkstra
    static ArrayList<String> domicilios = new ArrayList<String>();
      public static void main(String args[]) throws IOException {
            Scanner sc = new Scanner(System.in);
            Grafo g = new Grafo();
            String file = "medellin_arcos.txt";
            String file1 = "prueba3.txt";
            String sCadena = "";
            String sCadena1 = "";
            try{
                 BufferedReader buffer = new BufferedReader(new FileReader(file));
                 sCadena = buffer.readLine();
                 int tamano = Integer.parseInt(sCadena);
                 System.out.println("Este es el tamaño: "+tamano);
                 int[] nodos = new int[tamano];
                 int [][] adyacencia = new int [tamano][tamano];
                 while ((sCadena = buffer.readLine())!=null ) {
                   String [] numeros = sCadena.split(" ");
                   adyacencia[parseInt(numeros[0])][parseInt(numeros[1])]= parseInt(numeros[2]);
                   nodos[parseInt(numeros[0])] = parseInt(numeros[0]);
                   nodos[parseInt(numeros[1])] = parseInt(numeros[1]);
                   //adyacencia[parseInt(numeros[1])][parseInt(numeros[0])]= parseInt(numeros[2]);
                 }
                 
                /* System.out.println("Matriz costos");
                 for(int i=0; i < tamano; i++){
                     System.out.println(" ");
                     for(int j=0; j< tamano; j++){
                         System.out.print(" ");
                         System.out.print(adyacencia[i][j]);
                        }
                    }
                 System.out.println(" ");]
                 
                 System.out.println("Array nodos");
                 for(int j=0; j<nodos.length;j++){
                     System.out.print(nodos[j]);
                 }
                 System.out.println(" ");*/
                
                 g.grafo(nodos,adyacencia);
               }catch (FileNotFoundException e) {
                 System.out.println("No encuentra el fichero 1");
               }
            try{
                BufferedReader buffer = new BufferedReader(new FileReader(file1));
                 sCadena1 = buffer.readLine();
                 int tamano = Integer.parseInt(sCadena1);
                 
                 System.out.println("Estos son la cantidad de repartidores: "+tamano);
                 while ((sCadena1 = buffer.readLine())!=null ) {
                   domicilios.add(sCadena1);
                 }
                /* for(int i=0; i < domicilios.size();i++){
                     int ls = 0;
                     ls = i+1;
                     System.out.println("Repartidor["+ls+"] = "+domicilios.get(i));
                 }*/
            }catch(FileNotFoundException e){
                System.out.println("No encuentra el fichero 2");
            }
            String total = "";
            String ns = "";
            int costo = 0;
            for (int i=0; i<domicilios.size(); ++i) {
                String dm = domicilios.get(i);
                String[] nods = dm.split(" ");
                for (int j=0; j<nods.length; ++j) {
                   if(j+1 != nods.length) {
                        String res = g.estIncio2(Integer.parseInt(nods[j]), Integer.parseInt(nods[j+1]));
                        String val[] = res.split(":");
                        costo = costo + Integer.parseInt(val[0]);
                        ns = ns + val[1];
                   } 
                }
                int cn = i+1;
                System.out.println("|||| PARA REPARTIDOR |||| ["+cn+"]");
                System.out.println("Ruta de ida -> "+costo+":"+ns);
                System.out.println("Ruta de vuelta -> " + g.estIncio2(Integer.parseInt(nods[nods.length-1]), Integer.parseInt(nods[0])));
                costo = 0;
                ns = "";
            }
            
        }
     
       public void grafo(int[] serieNodos, int [][] costos) {
           Scanner sc = new Scanner(System.in); 
           int tam = serieNodos.length;
            nodos = new int[tam];
            for (int i=0; i<tam; ++i) {
                nodos[i] = serieNodos[i];
            }
            grafo = new int[nodos.length][nodos.length];
            for(int j=0;j<grafo.length;j++){
                for(int k=0; k< grafo.length;k++){
                grafo[j][k] = costos[j][k];
                }
            }
            /*System.out.println("Ingrese el nodo donde empieza");
            int a = sc.nextInt();
            System.out.println("Ingrese el nodo donde termina");
            int b = sc.nextInt();
            EstInicio(a,b);*/
        }
      /*  public void EstInicio(int a, int b){
            int inicio = a;
            int fin    = b;
            String respuesta = this.encontrarRutaMinimaDijkstra(inicio, fin);
            System.out.println(respuesta);
        }*/
        public String estIncio2(int a, int b) {
            return this.encontrarRutaMinimaDijkstra(a, b);  
        }
        
        public String encontrarRutaMinimaDijkstra(int inicio, int fin) {
        // calcula la ruta más corta del inicio a los demás
        encontrarRutaMinimaDijkstra(inicio);
        // recupera el nodo final de la lista de terminados
        Nodo tmp = new Nodo(fin);
        if(!listos.contains(tmp)) {
            System.out.println("Error, nodo no conectado");
            return "Bye";
        }
        tmp = listos.get(listos.indexOf(tmp));
        int distancia = tmp.distancia;  
        // crea una pila para almacenar la ruta desde el nodo final al origen
        Stack<Nodo> pila = new Stack<Nodo>();
        while(tmp != null) {
            pila.add(tmp);
            tmp = tmp.procedencia;
        }
        String ruta = "";
        // recorre la pila para armar la ruta en el orden correcto
        while(!pila.isEmpty()) 
        ruta+=(pila.pop().id + " ");
        return distancia + ": " + ruta;
    }
 
    // encuentra la ruta más corta desde el nodo inicial a todos los demás
    public void encontrarRutaMinimaDijkstra(int inicio) {
        Queue<Nodo> cola = new PriorityQueue<Nodo>(); // cola de prioridad
        Nodo ni = new Nodo(inicio);          // nodo inicial
         
        listos = new LinkedList<Nodo>();// lista de nodos ya revisados
        cola.add(ni);                   // Agregar nodo inicial a la cola de prioridad
        while(!cola.isEmpty()) {        // mientras que la cola no esta vacia
            Nodo tmp = cola.poll();     // saca el primer elemento
            listos.add(tmp);            // lo manda a la lista de terminados
            int p = posicionNodo(tmp.id);   
            for(int j=0; j<grafo[p].length; j++) {  // revisa los nodos hijos del nodo tmp
                if(grafo[p][j]==0) 
                continue;        // si no hay conexión no lo evalua
                if(estaTerminado(j)) 
                continue;      // si ya fue agregado a la lista de terminados
                Nodo nod = new Nodo(nodos[j],tmp.distancia+grafo[p][j],tmp);
                // si no está en la cola de prioridad, lo agrega
                if(!cola.contains(nod)) {
                    cola.add(nod);
                    continue;
                }
                // si ya está en la cola de prioridad actualiza la distancia menor
                for(Nodo x: cola) {
                    // si la distancia en la cola es mayor que la distancia calculada
                    if(x.id==nod.id && x.distancia > nod.distancia) {
                        cola.remove(x); // remueve el nodo de la cola
                        cola.add(nod);  // agrega el nodo con la nueva distancia
                        break;          // no sigue revisando
                    }
                }
            }
        }
    }
    private int posicionNodo(int nodo) {
            for(int i=0; i<nodos.length; i++) {
                if(nodos[i]==nodo) 
                return i;
            }
            return -1;
    }
    public boolean estaTerminado(int j) {
        Nodo tmp = new Nodo(nodos[j]);
        return listos.contains(tmp);
    }
 
    // recorre recursivamente las rutas entre un nodo inicial y un nodo final
    // almacenando en una cola cada nodo visitado
    private void recorrerRutas(int nodoI, int nodoF, Stack<Integer> resultado) {
        // si el nodo inicial es igual al final se evalúa la ruta en revisión
        if(nodoI==nodoF) {
            int respuesta = evaluar(resultado);
            if(respuesta < longitudMasCorta) {
                longitudMasCorta = respuesta;
                rutaMasCorta     = "";
                for(int x: resultado) 
                rutaMasCorta+=(nodos[x]+" ");
            }
            return;
        }
        // Si el nodoInicial no es igual al final se crea una lista con todos los nodos
        // adyacentes al nodo inicial que no estén en la ruta en evaluación
        List<Integer> lista = new Vector<Integer>();
        for(int i=0; i<grafo.length;i++) {
            if(grafo[nodoI][i]!=0 && !resultado.contains(i))lista.add(i);
        }
        // se recorren todas las rutas formadas con los nodos adyacentes al inicial
        for(int nodo: lista) {
            resultado.push(nodo);
            recorrerRutas(nodo, nodoF, resultado);
            resultado.pop();
        }
    }
 
    // evaluar la longitud de una ruta
    public int evaluar(Stack<Integer> resultado) {
        int resp = 0;
        int[] r = new int[resultado.size()];
        int i = 0;
        for(int x: resultado) 
            r[i++]=x;
        for(i=1; i<r.length; i++) 
            resp+=grafo[r[i]][r[i-1]];
        return resp;
    }
        
}


