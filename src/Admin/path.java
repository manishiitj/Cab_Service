/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;
/**
 *
 * @author BEDI
 */
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class path  {
    double min;
    static String []minpath;
    static String []currentpath;
    static int numlocation;
    static double totaldist;
    static String tos;
    static String type;
    static int links;
    /**
     * @param args the command line arguments
     */
    public path(String tos,String type)
    {
        this.tos=tos;
        this.type=type;
        min=2147483647;
        totaldist=2147483647;
        links=0;
    }
    public static void shortest(String start) {
        int x=0;
        int y=0;
        String cord="";
       //check availability of cab.
       //for each cab run path.
        /*
         * compare the co-ordinates of checkpoint with roads ;
         * apply distance formula to find distance.
         * go to other end of the road
         * check for presence of cab and its availability
         * create an array to enter row index
         * now check for other distance
         * break when distance is greater than current
         */
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost/cabservice", "root", "");
            Statement stat=con.createStatement();
            ResultSet r=stat.executeQuery("SELECT COUNT(name) FROM location");
            if(r.next())
            {
                numlocation=r.getInt(1);
                minpath=new String[r.getInt(1)];
                currentpath=new String[r.getInt(1)];
            }
            currentpath[0]=start;
            minpath(start,start,0,1);
            showpath();
            time();
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }
    public static double distance(String x,String y)
    {
        double dist=Math.sqrt(Math.pow((Integer.parseInt(x.substring(0, x.indexOf(",")))-Integer.parseInt(y.substring(0, y.indexOf(",")))), 2)+Math.pow((Integer.parseInt(x.substring(x.indexOf(",")+1))-Integer.parseInt(y.substring(y.indexOf(",")+1))), 2));
        return dist;
    }
    public static boolean iscab(String pos)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost/cabservice", "root", "");
            Statement stat=con.createStatement();
            ResultSet r=stat.executeQuery("SELECT * FROM cab_details WHERE current='"+pos+"'");
            if(r.next())
            {
                return true;
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }

        return false;
    }
    public static void minpath(String cord,String other,double distance,int index)
    {
        distance+=distance(cord,other);
        int x=Integer.parseInt(cord.substring(0,cord.indexOf(",")))+12;
        int y=Integer.parseInt(cord.substring(cord.indexOf(",")+1))+20;
        other=x+","+y;
        if(iscab(other)&&cabcheck(other))
        {
            if(distance<totaldist)
            {
                links=index;
                System.out.println(index);
                totaldist=distance;
                copy();
            }
            return;
        }
        else
        {
            String temp;
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost/cabservice", "root", "");
                Statement stat=con.createStatement();
                ResultSet r=stat.executeQuery("SELECT * FROM road WHERE source='"+other+"' OR destination='"+other+"'");
                while(r.next())
                {
                    if(!r.getString("source").equals(other))
                    {
                        x=Integer.parseInt(r.getString("source").substring(0,r.getString("source").indexOf(",")))-12;
                        y=Integer.parseInt(r.getString("source").substring(r.getString("source").indexOf(",")+1))-20;
                        temp=x+","+y;
                        if(check(index,temp))
                        {
                            currentpath[index]=temp;
                            minpath(temp,cord,distance,++index);
                        }
                    }
                    else
                    {
                        x=Integer.parseInt(r.getString("destination").substring(0,r.getString("destination").indexOf(",")))-12;
                        y=Integer.parseInt(r.getString("destination").substring(r.getString("destination").indexOf(",")+1))-20;
                        temp=x+","+y;
                        if(check(index,temp))
                        {
                            currentpath[index]=temp;
                            minpath(temp,cord,distance,++index);
                        }
                    }
                }
            }catch(Exception e)
            {
                System.out.println(e);
            }
            return;
        }
    }
    public static void copy()
    {
        for(int i=0;i<numlocation;i++)
        {
            minpath[i]=currentpath[i];
        }
    }
    public static boolean check(int index,String cord)
    {
        for(int i=0;i<index;i++)
        {
            if(currentpath[i].equals(cord))
                return false;
        }
        return true;
    }
    
    public static void showpath()
    {
        for(int i=0;i<links;i++)
        {
            System.out.print(minpath[i]+" ");
        }
    }
    public static boolean cabcheck(String cord)
    {
        Date d=new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String date=ft.format(d);
        try
        {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost/cabservice", "root", "");
        Statement stat=con.createStatement();
        ResultSet r=stat.executeQuery("SELECT * FROM cab_details WHERE current='"+cord+"' AND tos='"+tos+"' AND type='"+type+"'");
        while(r.next())
        {
            if(!r.getString("maintainance").equals("Required"))
            {
                Statement stat1=con.createStatement();
                ResultSet r1=stat1.executeQuery("SELECT * FROM bookings WHERE id='"+r.getString("id")+"'");
                if(r1.next())
                {
                        return true;
                }
                else
                {
                    return true;
                }
            }
        }
    }catch(Exception e)
    {
        System.out.println(e);
    }
    return false;
    }
    public static void time()
    {
    }
}