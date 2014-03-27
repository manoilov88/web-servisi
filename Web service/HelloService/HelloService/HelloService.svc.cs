using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using Npgsql;
using Mono.Security;
using System.Data;
using System.Xml.Linq;

namespace HelloService
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "HelloService" in code, svc and config file together.
    public class HelloService : IHelloService
    {
        public static string ConnectionString = "HOST=192.168.0.100;PORT=5432;PROTOCOL=3;DATABASE=Positioning;USER ID=postgres;PASSWORD='';";

        public string SayHello(String name, String language)
        {
            switch (language)
            { 
                case "en":
                    return "Hello " + name;
                case "es":
                    return "Hola " + name;
                default: 
                    return "unsuported language";
            }
        }

        public int Zbir(int a, int b)
        {
            return a + b;
        }

        public int Razlika(int a, int b)
        {
            return a - b;
        }

        public int Proizvod(int a, int b)
        {
            return a * b;
        }

        public double Kolicnik(int a, int b)
        {
            return a / b;
        }
            
        public string AllArchParks()
        {
            List<string> arch_parks = new List<string>();

            DataTable dt = DataBaseHelper.GetData(@"select name from ""Location""");
            XElement all_parks= new XElement("parks");

            for (int i = 0; i < dt.Rows.Count; i++)
            {
                arch_parks.Add(dt.Rows[i]["name"].ToString());
                XElement pregled =
                           new XElement("park",
                               new XElement("name", dt.Rows[i]["name"].ToString())
                           );
                all_parks.Add(pregled);
            }


            //return arch_parks;
            return all_parks.ToString();
        }

        public string AllParkObjects(string park_name)
        {
            List<string> arch_objects = new List<string>();
            string querry = @"select * from ""Coordinates"" where location_id = (Select id_location from ""Location"" where name = '" + park_name + "')"; 
            DataTable dt = DataBaseHelper.GetData(querry);

            XElement all_parks = new XElement("objects");

            for (int i = 0; i < dt.Rows.Count; i++)
            {
                arch_objects.Add(dt.Rows[i]["name"].ToString());
                XElement pregled =
                           new XElement("object",
                               new XElement("name", dt.Rows[i]["name"].ToString()),
                               new XElement("latitude", dt.Rows[i]["latitude"].ToString()),
                               new XElement("longitude", dt.Rows[i]["longitude"].ToString())
                           );
                all_parks.Add(pregled);
            }


            //return arch_parks;
            return all_parks.ToString();
        }

        public double distance(double lat1, double lon1, double lat2, double lon2, char unit) 
        {
	        double theta = lon1 - lon2;
	        double dist = Math.Sin(deg2rad(lat1)) * Math.Sin(deg2rad(lat2)) + Math.Cos(deg2rad(lat1)) * Math.Cos(deg2rad(lat2)) * Math.Cos(deg2rad(theta));
	        dist = Math.Acos(dist);
	        dist = rad2deg(dist);
	        dist = dist * 60 * 1.1515;
	        if (unit == 'K') {
	        dist = dist * 1.609344;
	        } else if (unit == 'N') {
	        dist = dist * 0.8684;
	        }
            else if (unit == 'M')
            {
                dist = dist * 1.609344;
                dist = dist * 1000;
            }
	        return (dist);
	    }

      

        public string distances(string lat1, string lon1, string park_name)
        {
            double tmp_lat1 = Convert.ToDouble(lat1.Replace(".",","));
            double tmp_lon1 = Convert.ToDouble(lon1.Replace(".",","));

            XElement all_distances = new XElement("distances");

            string querry = @"select * from ""Coordinates"" where location_id = (Select id_location from ""Location"" where name = '" + park_name + "')"; 
            DataTable dt = DataBaseHelper.GetData(querry);

            for (int i = 0; i < dt.Rows.Count; i++)
            {

                string dist = pom_distance(tmp_lat1, tmp_lon1, Convert.ToDouble(dt.Rows[i]["latitude"]), Convert.ToDouble(dt.Rows[i]["longitude"])).ToString();

                XElement distance =
                           new XElement("distance",
                               new XElement("name", dt.Rows[i]["name"].ToString()),
                               new XElement("meters", dist)
                           );
                all_distances.Add(distance);
            }

            return all_distances.ToString();
        }

        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	    //::  This function converts decimal degrees to radians             :::
	    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	    private double deg2rad(double deg) 
        {
          return (deg * Math.PI / 180.0);
	    }
	 	    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	    //::  This function converts radians to decimal degrees             :::
	    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	    private double rad2deg(double rad)
        {
	      return (rad / Math.PI * 180.0);
	    }

        private double pom_distance(double lat1, double lon1, double lat2, double lon2)
        {
            double theta = lon1 - lon2;
            double dist = Math.Sin(deg2rad(lat1)) * Math.Sin(deg2rad(lat2)) + Math.Cos(deg2rad(lat1)) * Math.Cos(deg2rad(lat2)) * Math.Cos(deg2rad(theta));
            dist = Math.Acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;
            dist = dist * 1000;
            return (dist);
        }
    }
}
