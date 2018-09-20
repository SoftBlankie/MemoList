package com.example.memolist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Kinred on 5/8/18.
 */

public class data_sort {

    public static void sortByImportance(ArrayList<data_model> dataModels, ArrayList<ArrayList<data_model>> categoryDataModels) {
        Collections.sort(dataModels, new Comparator<data_model>() {
            @Override
            public int compare(data_model o1, data_model o2) {
                int o1Int;
                int o2Int;

                if (o1.getImportance() == null){
                    o1Int = 0;
                } else {
                    o1Int = Integer.parseInt(o1.getImportance());
                }
                if (o2.getImportance() == null) {
                    o2Int = 0;
                } else {
                    o2Int = Integer.parseInt(o2.getImportance());
                }
                if (o1Int == o2Int) {
                    return 0;
                }

                return (o1Int > o2Int) ? -1 : 1;
            }
        });

        for (int i = 0; i < categoryDataModels.size(); i++) {
            Collections.sort(categoryDataModels.get(i), new Comparator<data_model>() {
                @Override
                public int compare(data_model o1, data_model o2) {
                    int o1Int;
                    int o2Int;

                    if (o1.getImportance() == null){
                        o1Int = 0;
                    } else {
                        o1Int = Integer.parseInt(o1.getImportance());
                    }
                    if (o2.getImportance() == null) {
                        o2Int = 0;
                    } else {
                        o2Int = Integer.parseInt(o2.getImportance());
                    }
                    if (o1Int == o2Int) {
                        return 0;
                    }

                    return (o1Int > o2Int) ? -1 : 1;
                }
            });
        }
    }

    public static void sortByAlphabet(ArrayList<data_model> dataModels, ArrayList<ArrayList<data_model>> categoryDataModels) {
        Collections.sort(dataModels, new Comparator<data_model>() {
            @Override
            public int compare(data_model o1, data_model o2) {
                return o1.getMessage().compareTo(o2.getMessage());
            }
        });

        for (int i = 0; i < categoryDataModels.size(); i++) {
            Collections.sort(categoryDataModels.get(i), new Comparator<data_model>() {
                @Override
                public int compare(data_model o1, data_model o2) {
                    return o1.getMessage().compareTo(o2.getMessage());
                }
            });
        }

    }

    public static void sortByDateEdit(ArrayList<data_model> dataModels, ArrayList<ArrayList<data_model>> categoryDataModels) {
        Collections.sort(dataModels, new Comparator<data_model>() {
            @Override
            public int compare(data_model o1, data_model o2) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                try {
                    return df.parse(o2.getDateEdited()).compareTo(df.parse(o1.getDateEdited()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        for (int i = 0; i < categoryDataModels.size(); i++) {
            Collections.sort(categoryDataModels.get(i), new Comparator<data_model>() {
                @Override
                public int compare(data_model o1, data_model o2) {
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    try {
                        return df.parse(o2.getDateEdited()).compareTo(df.parse(o1.getDateEdited()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
        }

    }

}
