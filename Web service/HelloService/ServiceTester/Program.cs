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
                Console.WriteLine(hs.AllArchParks());
                Console.WriteLine(hs.AllParkObjects("Tvrđava"));

                Console.WriteLine(hs.distance(43.324083, 21.901384, 43.324419, 21.894635, 'M'));
                Console.WriteLine(hs.distance(32.9697, -96.80322, 29.46786, -98.53506, 'K'));
                Console.WriteLine(hs.distance(32.9697, -96.80322, 29.46786, -98.53506, 'N'));
                Console.WriteLine(hs.distances("43.3241957", "21.9012651", "Tvrđava"));
                Console.Read();
            }
            
        }
    }
}
