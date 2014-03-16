using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace HelloService
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "HelloService" in code, svc and config file together.
    public class HelloService : IHelloService
    {
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
    }
}
