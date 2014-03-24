using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using Npgsql;
using Mono.Security;
using System.Data;

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

            DataTable dt = DataBaseHelper.GetData(@"select * from ""Location""");
            
            arch_parks.Add("Tvrđava");
            arch_parks.Add("Medijana");

            //return arch_parks;
            return dt.Rows[0]["name"].ToString();
        }
    }
}
