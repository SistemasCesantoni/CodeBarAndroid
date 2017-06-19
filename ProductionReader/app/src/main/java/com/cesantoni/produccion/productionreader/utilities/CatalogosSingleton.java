package com.cesantoni.produccion.productionreader.utilities;

import com.cesantoni.produccion.productionreader.dao.Calibre;
import com.cesantoni.produccion.productionreader.dao.Tono;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by liel on 09/05/2017.
 */

@SuppressWarnings("serial")
public class CatalogosSingleton implements Serializable{
    private static CatalogosSingleton uniqInstance;

    private HashMap<String, String> presentaciones;
    private ArrayList<Tono> tonos;
    private ArrayList<Calibre> calibres;

    private CatalogosSingleton() {
        setPresentaciones();
        setTonos();
        setCalibres();
    }

    public static CatalogosSingleton getInstance() {
        if (uniqInstance == null) {
            synchronized(CatalogosSingleton.class) {
                if (uniqInstance == null)
                    uniqInstance = new CatalogosSingleton();
            }
        }
        return uniqInstance;
    }

    public HashMap<String, String> getPresentaciones() {
        return presentaciones;
    }

    public ArrayList<Tono> getTonos() {
        return tonos;
    }

    public ArrayList<Calibre> getCalibres() {
        return calibres;
    }

    private void setPresentaciones() {
        presentaciones = new HashMap<>();
        presentaciones.put("00", "Fletes");
        presentaciones.put("01", "1.00/Unidad");
        presentaciones.put("02", "1.00/72");
        presentaciones.put("03", "1.44/48");
        presentaciones.put("04", "1.46/24");
        presentaciones.put("05", "1.50/24");
        presentaciones.put("06", "1.50/48");
        presentaciones.put("07", "1.50/66");
        presentaciones.put("08", "1.50/78");
        presentaciones.put("09", "1.50/81");
        presentaciones.put("10", "2.00/48");
        presentaciones.put("11", "1.50/24");
        presentaciones.put("12", "1.46/24");
        presentaciones.put("13", "1.36/32");
        presentaciones.put("14", "1.50/32");
        presentaciones.put("15", "1.47/32");
        presentaciones.put("16", "1.65/24");
        presentaciones.put("17", "1.44/40");
        presentaciones.put("18", "1.00/60");
        presentaciones.put("19", ".38/40");
        presentaciones.put("20", "1.00/36");
        presentaciones.put("21", "1.00/32");
        presentaciones.put("22", "1.53/32");
        presentaciones.put("23", ".26/40");
        presentaciones.put("24", "1.00/36");
        presentaciones.put("25", "1.00/32");
        presentaciones.put("26", "1.50/32");
        presentaciones.put("27", "1.53/32");
        presentaciones.put("28", ".22/40");
        presentaciones.put("29", ".49/40");
        presentaciones.put("30", "1.92/28");
        presentaciones.put("31", "1.02/50");
        presentaciones.put("32", ".49/84");
        presentaciones.put("33", ".49/64");
        presentaciones.put("34", ".2187/120");
        presentaciones.put("35", "1.13/24");
        presentaciones.put("36", "1.11/24");
        presentaciones.put("37", "1.09/24");
        presentaciones.put("38", "1.215/40");
        presentaciones.put("39", "1.08/24");
        presentaciones.put("40", "1.620/22");
        presentaciones.put("41", "1.20/24");
        presentaciones.put("42", "1.55/22");
        presentaciones.put("43", "1.23/52");
        presentaciones.put("44", ".47/84");
    }

    private void setTonos() {
        tonos = new ArrayList<>();
        tonos.add(new Tono("00", "Fletes"));
        tonos.add(new Tono("01", "1"));
        tonos.add(new Tono("02", "2"));
        tonos.add(new Tono("03", "3"));
        tonos.add(new Tono("04", "4"));
        tonos.add(new Tono("05", "5"));
        tonos.add(new Tono("06", "6"));
        tonos.add(new Tono("07", "7"));
        tonos.add(new Tono("08", "8"));
        tonos.add(new Tono("09", "9"));
        tonos.add(new Tono("0A", "091"));
        tonos.add(new Tono("0B", "0111"));
        tonos.add(new Tono("10", "10"));
        tonos.add(new Tono("11", "11"));
        tonos.add(new Tono("13", "00"));
        tonos.add(new Tono("14", "000"));
        tonos.add(new Tono("15", "01"));
        tonos.add(new Tono("16", "02"));
        tonos.add(new Tono("17", "03"));
        tonos.add(new Tono("18", "04"));
        tonos.add(new Tono("19", "05"));
        tonos.add(new Tono("20", "06"));
        tonos.add(new Tono("21", "07"));
        tonos.add(new Tono("22", "08"));
        tonos.add(new Tono("23", "09"));
        tonos.add(new Tono("24", "010"));
        tonos.add(new Tono("25", "011"));
        tonos.add(new Tono("26", "012"));
        tonos.add(new Tono("27", "013"));
        tonos.add(new Tono("28", "014"));
        tonos.add(new Tono("29", "015"));
        tonos.add(new Tono("30", "016"));
        tonos.add(new Tono("31", "017"));
        tonos.add(new Tono("32", "018"));
        tonos.add(new Tono("33", "019"));
        tonos.add(new Tono("34", "020"));
        tonos.add(new Tono("35", "021"));
        tonos.add(new Tono("36", "022"));
        tonos.add(new Tono("37", "023"));
        tonos.add(new Tono("38", "024"));
        tonos.add(new Tono("39", "025"));
        tonos.add(new Tono("40", "026"));
        tonos.add(new Tono("41", "027"));
        tonos.add(new Tono("42", "028"));
        tonos.add(new Tono("43", "029"));
        tonos.add(new Tono("44", "030"));
        tonos.add(new Tono("45", "031"));
        tonos.add(new Tono("46", "032"));
        tonos.add(new Tono("47", "033"));
        tonos.add(new Tono("48", "034"));
        tonos.add(new Tono("49", "035"));
        tonos.add(new Tono("50", "036"));
        tonos.add(new Tono("51", "037"));
        tonos.add(new Tono("52", "039"));
        tonos.add(new Tono("53", "040"));
        tonos.add(new Tono("54", "041"));
        tonos.add(new Tono("55", "042"));
        tonos.add(new Tono("56", "044"));
        tonos.add(new Tono("57", "045"));
        tonos.add(new Tono("58", "046"));
        tonos.add(new Tono("59", "050"));
        tonos.add(new Tono("60", "051"));
        tonos.add(new Tono("61", "L.E."));
        tonos.add(new Tono("62", "L.U."));
        tonos.add(new Tono("63", "038"));
        tonos.add(new Tono("64", "043"));
        tonos.add(new Tono("65", "047"));
        tonos.add(new Tono("66", "048"));
        tonos.add(new Tono("67", "049"));
        tonos.add(new Tono("68", "052"));
        tonos.add(new Tono("69", "053"));
        tonos.add(new Tono("70", "054"));
        tonos.add(new Tono("71", "055"));
        tonos.add(new Tono("72", "056"));
        tonos.add(new Tono("73", "058"));
        tonos.add(new Tono("74", "073"));
        tonos.add(new Tono("75", "060"));
        tonos.add(new Tono("76", "065"));
        tonos.add(new Tono("77", "064"));
        tonos.add(new Tono("78", "063"));
        tonos.add(new Tono("79", "067"));
        tonos.add(new Tono("80", "068"));
        tonos.add(new Tono("81", "070"));
        tonos.add(new Tono("82", "057"));
        tonos.add(new Tono("83", "072"));
        tonos.add(new Tono("84", "074"));
        tonos.add(new Tono("85", "075"));
        tonos.add(new Tono("86", "079"));
        tonos.add(new Tono("87", "081"));
        tonos.add(new Tono("88", "061"));
        tonos.add(new Tono("89", "12"));
        tonos.add(new Tono("90", "13"));
        tonos.add(new Tono("91", "062"));
        tonos.add(new Tono("92", "082"));
        tonos.add(new Tono("93", "077"));
        tonos.add(new Tono("94", "085"));
        tonos.add(new Tono("95", "086"));
        tonos.add(new Tono("96", "087"));
        tonos.add(new Tono("97", "089"));
        tonos.add(new Tono("98", "41"));
        tonos.add(new Tono("99", "15"));
        tonos.add(new Tono("P1", "3.8"));
        tonos.add(new Tono("P2", "4.8"));
        tonos.add(new Tono("P3", "20"));
        tonos.add(new Tono("P4", "4.08"));
        tonos.add(new Tono("P5", "3.8 (U) II"));
    }

    private void setCalibres() {
        calibres = new ArrayList<>();
        calibres.add(new Calibre("39", "A"));
        calibres.add(new Calibre("01", "B"));
        calibres.add(new Calibre("14", "B/C"));
        calibres.add(new Calibre("02", "C"));
        calibres.add(new Calibre("15", "C/D"));
        calibres.add(new Calibre("03", "D"));
        calibres.add(new Calibre("16", "D/E"));
        calibres.add(new Calibre("04", "E"));
        calibres.add(new Calibre("21", "E/F"));
        calibres.add(new Calibre("05", "F"));
        calibres.add(new Calibre("22", "F/G"));
        calibres.add(new Calibre("17", "G"));
        calibres.add(new Calibre("23", "G/H"));
        calibres.add(new Calibre("18", "H"));
        calibres.add(new Calibre("19", "I"));
        calibres.add(new Calibre("24", "N"));
        calibres.add(new Calibre("25", "O"));
        calibres.add(new Calibre("47", "O/P"));
        calibres.add(new Calibre("40", "O/Q"));
        calibres.add(new Calibre("06", "P"));
        calibres.add(new Calibre("26", "P/Q"));
        calibres.add(new Calibre("42", "P/R"));
        calibres.add(new Calibre("41", "P/T"));
        calibres.add(new Calibre("07", "Q"));
        calibres.add(new Calibre("27", "Q/S"));
        calibres.add(new Calibre("08", "R"));
        calibres.add(new Calibre("28", "R/S"));
        calibres.add(new Calibre("46", "R/T"));
        calibres.add(new Calibre("09", "S"));
        calibres.add(new Calibre("13", "S/T"));
        calibres.add(new Calibre("43", "S/U"));
        calibres.add(new Calibre("10", "T"));
        calibres.add(new Calibre("44", "T/V"));
        calibres.add(new Calibre("50", "T/W"));
        calibres.add(new Calibre("11", "U"));
        calibres.add(new Calibre("29", "U/V"));
        calibres.add(new Calibre("45", "U/W"));
        calibres.add(new Calibre("12", "V"));
        calibres.add(new Calibre("49", "V/U"));
        calibres.add(new Calibre("30", "V/W"));
        calibres.add(new Calibre("48", "V/X"));
        calibres.add(new Calibre("31", "W"));
        calibres.add(new Calibre("32", "W/X"));
        calibres.add(new Calibre("33", "X"));
        calibres.add(new Calibre("35", "Y"));
        calibres.add(new Calibre("37", "Z"));
    }
}
