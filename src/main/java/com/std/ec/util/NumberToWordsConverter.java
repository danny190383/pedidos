package com.std.ec.util;

import java.math.BigDecimal;

public class NumberToWordsConverter {
    private static final String[] UNIDADES = {
            "", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"
    };

    private static final String[] DECENAS = {
            "", "diez", "veinte", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta", "ochenta", "noventa"
    };

    private static final String[] ESPECIALES = {
            "once", "doce", "trece", "catorce", "quince", "dieciséis", "diecisiete", "dieciocho", "diecinueve"
    };

    private static final String[] CENTENAS = {
            "", "ciento", "doscientos", "trescientos", "cuatrocientos", "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos"
    };

    public static String convertir(BigDecimal numero) {
        if (numero == null) {
            return "Cero con 00/100";
        }

        // Separar la parte entera y la parte decimal
        long entero = numero.longValue();
        int decimal = numero.remainder(BigDecimal.ONE).movePointRight(2).intValue();

        if (entero == 0) {
            return "Cero con " + String.format("%02d", decimal) + "/100";
        }

        String palabras = convertirEntero(entero) + " con " + String.format("%02d", decimal) + "/100";
        return palabras.trim();
    }

    private static String convertirEntero(long numero) {
        if (numero < 10) {
            return UNIDADES[(int) numero];
        } else if (numero < 20) {
            return ESPECIALES[(int) (numero - 11)];
        } else if (numero < 100) {
            return DECENAS[(int) (numero / 10)] + (numero % 10 != 0 ? " y " + UNIDADES[(int) (numero % 10)] : "");
        } else if (numero < 1000) {
            return CENTENAS[(int) (numero / 100)] + (numero % 100 != 0 ? " " + convertirEntero(numero % 100) : "");
        } else if (numero < 1000000) {
            return convertirEntero(numero / 1000) + " mil" + (numero % 1000 != 0 ? " " + convertirEntero(numero % 1000) : "");
        } else if (numero < 1000000000) {
            return convertirEntero(numero / 1000000) + " millón" + (numero % 1000000 != 0 ? " " + convertirEntero(numero % 1000000) : "");
        } else {
            return convertirEntero(numero / 1000000000) + " mil millones" + (numero % 1000000000 != 0 ? " " + convertirEntero(numero % 1000000000) : "");
        }
    }
}
