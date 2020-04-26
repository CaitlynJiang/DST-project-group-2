package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVwriter {
    /**
     * Copied from https://blog.csdn.net/luanpeng825485697/article/details/78162459
     * @param data
     * @param path
     */
    public static void Array2CSV(ArrayList<ArrayList<String>> data, String path)
    {
        try {
            BufferedWriter out =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"UTF-8"));
            for (int i = 0; i < data.size(); i++)
            {
                ArrayList<String> onerow=data.get(i);
                for (int j = 0; j < onerow.size(); j++)
                {
                    out.write(DelQuota(onerow.get(j)));
                    out.write(",");
                }
                out.newLine();
            }
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String DelQuota(String str)
    {
        String result = str;
        String[] strQuota = { "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "`", ";", "'", ",", ".", "/", ":", "/,", "<", ">", "?" };
        for (int i = 0; i < strQuota.length; i++)
        {
            if (result.indexOf(strQuota[i]) > -1)
                result = result.replace(strQuota[i], "");
        }
        return result;
    }

    public static ArrayList<ArrayList<String>> CSV2Array(String path)
    {
        try {
            BufferedReader in =new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
            ArrayList<ArrayList<String>> alldata=new ArrayList<ArrayList<String>>();
            String line;
            String[] onerow;
            while ((line=in.readLine())!=null) {
                onerow = line.split(",");  //默认分割符为逗号，可以不使用逗号
                List<String> onerowlist = Arrays.asList(onerow);
                ArrayList<String> onerowaArrayList = new ArrayList<String>(onerowlist);
                alldata.add(onerowaArrayList);
            }
            in.close();
            return alldata;
        } catch (Exception e) {
            return null;
        }

    }

    //       ArrayList<ArrayList<String>> dataFrame = new ArrayList<>();
//        for (ClinicAnnBean clinicAnnBean:matched_beans){
//            ArrayList<String> row = new ArrayList<>();
//            row.add(clinicAnnBean.getLocation());
//            row.add(clinicAnnBean.getGene());
//            row.add(clinicAnnBean.getSample_location());
//            row.add(clinicAnnBean.getSample_allele());
//            row.add(clinicAnnBean.getId());
//            dataFrame.add(row);
//        }
}
