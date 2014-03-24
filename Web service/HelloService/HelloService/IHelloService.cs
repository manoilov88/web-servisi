using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace HelloService
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IHelloService" in both code and config file together.
    [ServiceContract]
    public interface IHelloService
    {
        [OperationContract]
        string SayHello(String name, String language);

        [OperationContract]
        int Zbir(int a, int b);

        [OperationContract]
        int Razlika(int a, int b);

        [OperationContract]
        int Proizvod(int a, int b);

        [OperationContract]
        double Kolicnik(int a, int b);

        [OperationContract]
        string AllArchParks();
    }
}
