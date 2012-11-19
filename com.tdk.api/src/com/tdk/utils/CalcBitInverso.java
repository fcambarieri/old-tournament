/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p> Esta clase arma el algoritmo de bit inverso, que permite obtener los
 *  indices para un array que se quiere desordenar.
 * @author fcambarieri
 */
public class CalcBitInverso {

    /**
     *  Devuelve un nuevo array de booleans con los indices invertidos,
     *  es decir, el valor de booleans[0] pasa a la posición booleans[n-1]
     *  siendo <l>n<l> el largo de la cadena.
     *  @param array de booleans
     *  @return retorna un nuevo array invertido.
     *
     *  <p> Ejemplo: sea el siguiente array de booleans booleans b[] = {true, false , false}
     *  el método devuelve un newB = {false, false , true}
     */
    public static boolean[] inverse(boolean[] booleans) {
        boolean[] temp = new boolean[booleans.length];
        for (int i = 0; i < booleans.length; i++) {
            temp[booleans.length - i - 1] = booleans[i];
        }
        return temp;
    }

    private static String fillWithRightZeros(String value, int length) {
        if (value.length() == length) {
            return value;
        }
        if (value.length() == 1 && value.charAt(0) == '1') {
            value = "0" + value;
        }
        for (int i = 0; i <= (length - value.length()); i++) {
            value = "0" + value;
        }

        if (value.length() < length) {
            value = value + "0";
        }

        return value;
    }

    /**
     *  Aqui se convierte un array de booleans que representa un entero
     *  a un enterio propiamente dicho.
     *  @param  un array de booleans, por ejemplo b[] = {true, false, true}
     *  @return el valor que representa ese array
     * 
     *  <p> A un entero se lo puede representar de la siguiente manera en base 2 <br>
     *  5 = 2^2 + 2^0 que es equivalente a decir 101 en binario
     *  7 = 2^2 + 2^1 + 2^0 que es equivalente a decir 111 en binario
     */
    public static int convertBooleanArrayToInt(boolean[] b) {
        int x = 0;
        for (int i = 0; i < b.length; i++) {
            if (b[i]) {
                x += Math.pow(2, b.length - i - 1);
            }
        }
        return x;
    }

    /**
     *  Este método recibe un valor como parametro y devuelve un array de enteros
     *  con la sequencia del bit inverso.
     *  @param un valor entero
     *  @return un array de enteros con los valores enteros de dicho bit inverso
     *
     *  <p> <b>Ejemplo: </b> <br>
     *  <b>int</b> indeces[] = <b>bitInverse</b>(17);<br>
     *  Devuelve<br>
     *  Decimal - binario - bit inverso(decimal)<br>
     *   0    ....  00000            0<br>
     *   1    ....  00010            8<br>
     *   2    ....  00100            4<br>
     *   3    ....  00110           12<br>
     *   4          00100            4<br>
     *   5          00101           20<br>
     *   6          00110           12<br>
     *   7          00111           28<br>
     *  ..          .....           ..<br>
     *  17          10001           17
     */
    public static int[] bitInverse(int valor) {

        //obtengo la cantidad debits
        int countBits = Integer.toBinaryString(valor).length();

        //creo el arreglo con la cantidad de bits
        int[] inverse = new int[valor];

        for (int i = 0; i < valor; i++) {

            //transformo un valor decimal a su correspondiente en boolean
            //por medio de un String
            String binary = Integer.toBinaryString(i);

            //lleno el string con los ceros faltantes
            binary = fillWithRightZeros(binary, countBits);

            //creo un array de boolean
            boolean b[] = new boolean[countBits];

            for (int j = 0; j < countBits; j++) {

                if (binary.length() > j) {
                    b[j] = (binary.charAt(j) == '1');
                } else {
                    b[j] = false;
                }
            }

            //invierto el orden de los bits
            b = inverse(b);

            //convierto la cadena de bits a entero
            inverse[i] = convertBooleanArrayToInt(b);
        }

        return inverse;
    }

    public static List orderList(List list) {
        int[] index = CalcBitInverso.bitInverse(list.size());
        List<BitInverseItem> items = new ArrayList<BitInverseItem>();
        for (int i = 0; i < index.length; i++) {
            items.add(new BitInverseItem(list.get(i), index[i]));
        }

        Collections.sort(items, new Comparator<BitInverseItem>() {

            public int compare(BitInverseItem arg0, BitInverseItem arg1) {
                return arg0.getIndexValue().compareTo(arg1.getIndexValue());
            }
        });

        list.clear();

        for (BitInverseItem i : items) {
            list.add(i.getValue());
        }

        return list;
    }
}

class BitInverseItem {

    private Integer indexValue;
    private Object value;

    public BitInverseItem(Object value, int index) {
        this.value = value;
        this.indexValue = new Integer(index);
    }

    public Integer getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(Integer indexValue) {
        this.indexValue = indexValue;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}