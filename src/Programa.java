//////////////////////////////////////////////////////////////////
//                                                              //
//             SISTEMA DE INFORMACION - ZAPATERIA               //
//                                                              //
//                                                              //
//                                                              //
//                   DAVID ARMENTEROS SOTO                      //
//		     IÑAKI MELGUIZO MARCOS                      //
//                   CESAR MUÑOZ REINOSO                        //
//                                                              //
//                                                              //
//                                         2020-2021            //
//////////////////////////////////////////////////////////////////

import java.sql.SQLException;
import java.text.ParseException;

import java.util.Scanner;

import DAO.ZapateriaDAO;


public class Programa {
	public  static void main(String[] args) throws SQLException, ParseException {
	    ZapateriaDAO ZapDAO = new ZapateriaDAO ("jdbc:oracle:thin:@//oracle0.ugr.es:1521/practbd.oracle0.ugr.es", "x2090924", "x2090924");
	    ZapDAO.connect();
	    
	    try (Scanner input = new Scanner (System.in)) {
			int numero = 0;
			
			
			while(numero!=5){
			    // Menu
			    System.out.print("\n\n\nEstas son las funcionalidades disponibles:\n");
			    System.out.print("1: Clientes .\n");
			    System.out.print("2: Ventas .\n");
			    System.out.print("3: Pedidos .\n");
			    System.out.print("4: Promociones .\n");
			    
			    System.out.print("5: Salir del programa y cerrar conexión.\n");
			   
			    System.out.print("Indique el numero de la actividad que desea realizar: ");
			    
			    numero = input.nextInt();
			    if (numero==1) {		        	
			        int numero1=0;
			    	while (numero1 != 5) {
			    		System.out.print("1.1: Registrar cliente .\n");
			    		System.out.print("1.2: Modificar correo cliente .\n");
			    		System.out.print("1.3: Eliminar cliente .\n");
			    		System.out.print("1.4: Mostrar datos cliente .\n");
			    		System.out.print("1.5: Volver al menú principal.\n");
				        System.out.print("Indique el numero de la actividad que desea realizar: ");

			    		numero1 = input.nextInt();

			    		if(numero1 == 1) {

			    			String nombre = null;
			    			String correo = null;
			    			int tlf = 0;
			    			System.out.print("Ingrese el nombre del cliente .\n");
			    			nombre = input.next();
			    			System.out.print("Ingrese el correo del cliente .\n");
			    			correo = input.next();
			    			System.out.print("Ingrese el telefono del cliente .\n");
			    			tlf = input.nextInt();	        			
				        	ZapDAO.registrarCliente(nombre,correo,tlf);	
							ZapDAO.commit();    				

			    		}else if(numero1 == 2) {
			    			int id = 0;
			    			String correo = null;
			    			System.out.print("Ingrese el id del cliente .\n");
			    			id = input.nextInt();
			    			System.out.print("Ingrese el correo nuevo del cliente .\n");
			    			correo = input.next();
				        	ZapDAO.modificarCliente(id,correo);
							ZapDAO.commit();    				

						}else if(numero1 == 3) {
			    			int id = 0;
			    			System.out.print("Ingrese el id del cliente .\n");
			    			id = input.nextInt();
				        	ZapDAO.eliminarCliente(id);	  
							ZapDAO.commit();    				

						}else if(numero1 == 4) {
			    			String correo = null;
			    			System.out.print("Ingrese el correo del cliente .\n");
			    			correo = input.next();
				        	ZapDAO.mostrarCliente(correo);	
							ZapDAO.commit();    				
						}
			    	}	        
			    }
			    else if (numero==2) {
			    	int numero2=0;
			    	while (numero2 != 6) {
			        	System.out.print("2.1: Venta directa de producto .\n");
			        	System.out.print("2.2: Consultar venta por cliente  .\n");
			        	System.out.print("2.3: Venta de producto reservado .\n");
			        	System.out.print("2.4: Realizar reserva .\n");
			        	System.out.print("2.5: Cancelar reserva .\n");
			    		System.out.print("2.6: Volver al menú principal.\n");
				        System.out.print("Indique el numero de la actividad que desea realizar: ");

			    		numero2 = input.nextInt();

			    		if(numero2 == 1) {
			    			int id = 0;
			    			int idpro = 0;
			    			int cant = 0;
			    			System.out.print("Ingrese el id del cliente .\n");
			    			id = input.nextInt();
			    			System.out.print("Ingrese el id del producto .\n");
			    			idpro = input.nextInt();
			    			System.out.print("Ingrese la cantidad .\n");	        		
			    			cant = input.nextInt();
			    			while(cant < 0) {
			        			cant = input.nextInt();
			    			}
				        	ZapDAO.ventaDirecta(id,idpro,cant);	
							ZapDAO.commit();    				

			    		}else if(numero2 == 2) {
			    			int id = 0;
			    			System.out.print("Ingrese el id del cliente .\n");
			    			id = input.nextInt();
				        	ZapDAO.ventasCliente(id);	
							ZapDAO.commit();    				

						}else if(numero2 == 3) {
			    			int id = 0;
			    			System.out.print("Ingrese el id de la reserva .\n");
			    			id = input.nextInt();
				        	ZapDAO.ventaReserva(id);	 
							ZapDAO.commit();    				

						}else if(numero2 == 4) {
			    			int id = 0;
			    			int idpro = 0;
			    			int cant = 0;
			    			System.out.print("Ingrese el id del cliente .\n");
			    			id = input.nextInt();
			    			System.out.print("Ingrese el id del producto .\n");
			    			idpro = input.nextInt();
			    			System.out.print("Ingrese la cantidad .\n");
			    			cant = input.nextInt();
			    			while(cant < 0) {
			        			cant = input.nextInt();
			    			}
				        	ZapDAO.realizarReserva(id,idpro,cant);
							ZapDAO.commit();    				

						}else if(numero2 == 5) {
			    			int id = 0;
			    			System.out.print("Ingrese el id de la reserva .\n");
			    			id = input.nextInt();
				        	ZapDAO.cancelarReserva(id);
							ZapDAO.commit();    				
						}
			    	}
			    }else if (numero==3) {
			        int numero3=0;
			        while (numero3 != 5) {
				        	System.out.print("3.1: Realizar un pedido .\n");
				        	System.out.print("3.2: Confirmar un pedido  .\n");
				        	System.out.print("3.3: Ver estado de pedido.\n");
				        	System.out.print("3.4: Devolver pedido .\n");
			        		System.out.print("3.5: Volver al menú principal.\n");
			    	        System.out.print("Indique el numero de la actividad que desea realizar: ");

			        		numero3 = input.nextInt();

			        		if(numero3 == 1) {
			        			int idpro = 0;
			        			int cant = 0;
			        			System.out.print("Ingrese el id del producto .\n");
			        			idpro = input.nextInt();
			        			System.out.print("Ingrese la cantidad .\n");
			        			cant = input.nextInt();
			        			while(cant < 0) {
				        			cant = input.nextInt();
			        			}
			    	        	ZapDAO.realizarPedido(idpro,cant);	
			    				ZapDAO.commit();    				

			        		}else if(numero3 == 2) {
			        			int idped = 0;
			        			System.out.print("Ingrese el id del pedido .\n");
			        			idped = input.nextInt();
			    	        	ZapDAO.confirmarPedido(idped);	
			    				ZapDAO.commit();    				

			    			}else if(numero3 == 3) {
			        			int idped = 0;
			        			System.out.print("Ingrese el id del pedido .\n");
			        			idped = input.nextInt();
			    	        	ZapDAO.verPedido(idped);
			    				ZapDAO.commit();    				

							}else if(numero3 == 4) {
			        			int idped = 0;
			        			System.out.print("Ingrese el id del pedido .\n");
			        			idped = input.nextInt();
			    	        	ZapDAO.devolverPedido(idped);
			    				ZapDAO.commit();    
							}
			        }
			    }else if (numero==4) {
			    	int numero4=0;
			    	while (numero4 != 4) {
			    			System.out.print("4.1: Promocion de un nuevo producto .\n");
			        		System.out.print("4.2: Liquidación por stock  .\n");
			        		System.out.print("4.3: Calificar proveedor.\n");
			        		System.out.print("4.4: Volver al menú principal.\n");
			    	        System.out.print("Indique el numero de la actividad que desea realizar: ");

			        		numero4 = input.nextInt();

			        		if(numero4 == 1) {
			        			int idpro = 0;
			        			int idprov = 0;
			        			int cant = 0;
			        			float precio = (float) 0.0;
			        			int tiempoexp = 0;
			        			System.out.print("Ingrese el id del producto .\n");
			        			idpro = input.nextInt();
			        			while(idpro < 0) {
			        				idpro = input.nextInt();
			        			}
			        			System.out.print("Ingrese el id del proveedor .\n");
			        			idprov = input.nextInt();
			        			System.out.print("Ingrese la cantidad .\n");
			        			cant = input.nextInt();
			        			while(cant < 0) {
				        			cant = input.nextInt();
			        			}
			        			System.out.print("Ingrese el precio .\n");
			        			precio = input.nextFloat();
			        			while(cant < 0) {
			        				precio = input.nextFloat();
			        			}
			        			System.out.print("Ingrese el tiempo de expiracion .\n");
			        			tiempoexp = input.nextInt();	
			        			while(tiempoexp < 0) {
			        				tiempoexp = input.nextInt();
			        			}
			        			ZapDAO.promocionProducto(idpro,idprov,cant,precio,tiempoexp);	
			    				ZapDAO.commit();    				

			        		}else if(numero4 == 2) {
			        			int numMeses = 0;
			        			int descuento = 0;
			        			System.out.print("Ingrese el numero de meses .\n");
			        			numMeses = input.nextInt();
			        			while(numMeses < 0) {
			        				numMeses = input.nextInt();
			        			}
			        			System.out.print("Ingrese el porcentaje de descuento .\n");
			        			descuento = input.nextInt();
			        			while(descuento < 0) {
			        				descuento = input.nextInt();
			        			}
			        			ZapDAO.liquidacionProducto(numMeses,descuento);	
			    				ZapDAO.commit();    				

			        		}else if(numero4 == 3) {
			        			int idprov = 0;
			        			System.out.print("Ingrese el id del proveedor .\n");
			        			idprov = input.nextInt();
			        			ZapDAO.calificarProveedor(idprov);	
			    				ZapDAO.commit();    				
			        		}
			    }
			}
			}
		}

        ZapDAO.disconnect();
	}
}
