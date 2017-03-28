/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_storage_assignment_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile ;
import java.util.Arrays;
import java.util.Scanner;
import data_storage_assignment_1.functions;



/**
 *
 * @author abdelrhman
 * 
 * ID_1 : 20110221 ## Name_1: Abd El-Rahman Omar Youssif
 * ID_2 : 20110224 ## Name_2 : Abd El-Rahman Mohammed Hussien
 * 
 */


public class Data_Storage_Assignment_1 {

    /**
     * @param args the command line arguments
     */
     
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        System.out.print("Assignment #1 \n");
        
        int num_of_runs ,num_of_ks = 1, search_id ;
        String file_name = "Index.bin";
        String Sortedfilename = "Sorted.bin";
        Scanner reader = new Scanner(System.in);
        String [] runs , sorted_runs ;
        
        System.out.print("Enter Number of runs: ");
        num_of_runs = reader.nextInt();
        runs = functions.DivideInputFileIntoRuns(file_name,num_of_runs);
        for (int i = 0 ; i < runs.length ; i++) System.out.println(runs[i]);       
        sorted_runs = functions.SortEachRunOnMemoryAndWriteItBack(runs);
        
        System.out.print("\nEnter Number of Ks: ");
        num_of_ks = reader.nextInt();
        functions.DoKWayMergeAndWriteASortedFile(sorted_runs,num_of_ks,Sortedfilename);
        
        System.out.print("\nEnter ID to search for: ");
        search_id = reader.nextInt();
        RandomAccessFile sorted = new RandomAccessFile("temp_run_0", "rw");
        functions.BinarySearchFor(sorted,search_id,0 , (int) sorted.length());
        
        
        System.out.println("\nDelete the runs? (1/0) ");
        if(reader.nextInt()== 1)
        {
            // Deleteing runs
            File file = new File ("temp_run_0");
            file.delete();
            for (int i = 0 ; i < runs.length ; i++)
            {
                File file1 = new File(runs[i]);
                File file2 = new File(sorted_runs[i]);
                file1.delete();
                file2.delete();
            }
        }
    }
    
}
