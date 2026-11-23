package org.uabc.ic.farklejavafx.modelo;

import java.util.ArrayList;

//Aqui se declaran los valores de puntuacion, cuanto vale cada dado y las variables varias que se utilizan.
public class Puntuacion {

    private static final String[] NOMBRES = {"", "1", "2", "3", "4", "5", "6"};

    //Metodo para evaluar si al lanzar un dado, sale uno de estos valores (ejemplo: escalera, cuatro par, etc)
    public static int calcular(ArrayList<Integer> valores) {
        int[] conteo = contar(valores);
        int total = 0;

        if (valores.size() == 6) {
            if (esEscalera(conteo) == true) {
                return 1500;
            }
            if (sonDosTercias(conteo) == true) {
                return 2500;
            }
            if (esCuatroConPar(conteo) == true) {
                return 1500;
            }
            if (sonTresPares(conteo) == true) {
                return 1500;
            }
        }

        //Si no se cumple ningun combo, evalua maualmente el puntaje
        for (int valor = 1; valor <= 6; valor++) {
            int cantidad = conteo[valor];

            if (cantidad >= 3) {
                total = total + puntosIguales(valor, cantidad);
            } else if (cantidad > 0) {
                if (valor == 1) {
                    total = total + (cantidad * 100);
                } else if (valor == 5) {
                    total = total + (cantidad * 50);
                } else {
                    return 0;
                }
            }
        }

        return total;
    }

    //Ciclo que divide cuantas valores de cada cada tiene, si a 2 de 1, 4 de 2 y asi.
    public static int[] contar(ArrayList<Integer> valores) {
        int[] conteo = new int[7];
        for (int valor : valores) {
            conteo[valor] = conteo[valor] + 1;
        }
        return conteo;
    }

    //Evalua cuantos numeros distintos aparecen exactamente x cantidad de veces
    private static int cuantosTienen(int[] conteo, int cantidad) {
        int total = 0;
        for (int valor = 1; valor <= 6; valor++) {
            if (conteo[valor] == cantidad) {
                total = total + 1;
            }
        }
        return total;
    }

    //Aqui es donde se declaran los metodos estaticos de cada combo

    private static boolean esEscalera(int[] conteo) {
        return cuantosTienen(conteo, 1) == 6;
    }

    private static boolean sonDosTercias(int[] conteo) {
        return cuantosTienen(conteo, 3) == 2;
    }

    private static boolean esCuatroConPar(int[] conteo) {
        return cuantosTienen(conteo, 4) == 1 && cuantosTienen(conteo, 2) == 1;
    }

    private static boolean sonTresPares(int[] conteo) {
        return cuantosTienen(conteo, 2) == 3;
    }

    //Puntos de tres o mas dados iguales
    private static int puntosIguales(int valor, int cantidad) {
        if (cantidad == 6) {
            return 3000;
        }
        if (cantidad == 5) {
            return 2000;
        }
        if (cantidad == 4) {
            return 1000;
        }
        if (valor == 1) {
            return 300;
        }
        return valor * 100;
    }

    //Se encarga de validar que en cada ronda se ejecuto una combinacion, sino lo hace regresara error
    public static boolean hayPuntos(ArrayList<Integer> valores) {
        int[] conteo = contar(valores);

        if (conteo[1] > 0 || conteo[5] > 0) {
            return true;
        }

        for (int valor = 1; valor <= 6; valor++) {
            if (conteo[valor] >= 3) {
                return true;
            }
        }

        if (valores.size() == 6 && calcular(valores) > 0) {
            return true;
        }

        return false;
    }

    //Aqui se describen con texto que combinaciones se hicieron
    public static String describir(ArrayList<Integer> valores) {
        int[] conteo = contar(valores);

        if (valores.size() == 6) {
            if (esEscalera(conteo) == true) {
                return "Escalera";
            }
            if (sonDosTercias(conteo) == true) {
                return "Dos tercias";
            }
            if (esCuatroConPar(conteo) == true) {
                return "Cuatro iguales con par";
            }
            if (sonTresPares(conteo) == true) {
                return "Tres pares";
            }
        }

        ArrayList<String> partes = new ArrayList<>();
        for (int valor = 1; valor <= 6; valor++) {
            int cantidad = conteo[valor];

            if (cantidad > 0) {
                String texto = NOMBRES[cantidad] + " " + valor;
                if (cantidad > 1) {
                    texto = texto + "s";
                }
                partes.add(texto);
            }
        }

        return String.join(" + ", partes);
    }

    // Lista los combos que se pueden hacer con estos dados, uno por línea, con sus puntos
    public static String combosPosibles(ArrayList<Integer> valores) {
        int[] conteo = contar(valores);
        ArrayList<String> lineas = new ArrayList<>();

        if (valores.size() == 6) {
            if (esEscalera(conteo) == true) {
                lineas.add("Escalera (1500 puntos)");
            }
            if (sonDosTercias(conteo) == true) {
                lineas.add("Dos tercias (2500 puntos)");
            }
            if (esCuatroConPar(conteo) == true) {
                lineas.add("Cuatro iguales con par (1500 puntos)");
            }
            if (sonTresPares(conteo) == true) {
                lineas.add("Tres pares (1500 puntos)");
            }
        }

        for (int valor = 1; valor <= 6; valor++) {
            int cantidad = conteo[valor];

            if (cantidad >= 3) {
                lineas.add(NOMBRES[cantidad] + " " + valor + "s (" + puntosIguales(valor, cantidad) + ")");
            } else if (cantidad > 0 && valor == 1) {
                lineas.add(cantidad + " de 1 (" + (cantidad * 100) + ")");
            } else if (cantidad > 0 && valor == 5) {
                lineas.add(cantidad + " de 5 (" + (cantidad * 50) + ")");
            }
        }

        if (lineas.isEmpty() == true) {
            return "Ninguno";
        }
        return String.join("\n", lineas);
    }
}