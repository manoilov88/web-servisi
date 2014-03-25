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
    }
}
