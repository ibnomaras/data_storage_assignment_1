  
            

/*
import java.io.RandomAccessFile;
import java.util.Arrays;

            int rec [][] = new int [K][3];
            int keys [] = new int [K];

            for (int j = 0 ; j < to_be_merged.length ; j ++)
            {
                RandomAccessFile tmp = new RandomAccessFile(to_be_merged[j], "rw");                
                tmp.seek(j*8);
                System.out.print("ID : " + tmp.readInt());
                keys[j] = rec[j][0] = tmp.readInt();
                tmp.seek((j*8)+4);
                System.out.println(" With offset: " + tmp.readInt());
                rec[j][1] = tmp.readInt();
                rec[j][2] = j; 
            }
            
            Arrays.sort(keys);
            
            for (int j = 0 ; j < keys.length ; j++)
            {
                if(rec[j][0] == keys[0])
                {
                    temp_rn.seek(temp_rn.length());
                    temp_rn.writeInt(rec[0][0]);
                    temp_rn.seek(temp_rn.length());
                    temp_rn.writeInt(rec[0][1]);
                }
            }




*/