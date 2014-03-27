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

        [OperationContract]
        string AllParkObjects(string park_name);

        [OperationContract]
        double distance(double lat1, double lon1, double lat2, double lon2, char unit);

        [OperationContract]
        string distances(string lat1, string lon1, string park_name);

    }
}
