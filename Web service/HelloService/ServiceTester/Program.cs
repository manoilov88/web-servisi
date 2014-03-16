using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ServiceTester.HelloService;
namespace ServiceTester
{
    class Program
    {
        static void Main(string[] args)
        {
            
            using (HelloServiceClient hs = new HelloServiceClient())
            {
                Console.WriteLine(hs.SayHello("Djole", "en"));
                Console.WriteLine(hs.Zbir(3, 4));
                Console.WriteLine(hs.Razlika(113, 35));
                Console.Read();
            }
            
        }
    }
}
