using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;

namespace ISF_WEB.Util
{
    public class StringManipulation
    {
        public string  Reverse(string value)
        {
            string reversedString = string.Empty;
            var arr = value.ToArray<char>();
            Array.Reverse(arr);
            reversedString = new string(arr);
            
            return reversedString;
        }
        public string ConvertStringToAsciiWithPipe(string value)
        {
            string asciiConvertedString = string.Empty;
            StringBuilder sb = new StringBuilder();
            byte[] ASCIIValues = Encoding.ASCII.GetBytes(value);
            foreach (byte b in ASCIIValues)
            {
                sb.Append(b);
                sb.Append('|');
            }
            asciiConvertedString = sb.ToString();
            asciiConvertedString.Remove(asciiConvertedString.Length - 1);
            byte[] byt = System.Text.Encoding.UTF8.GetBytes(asciiConvertedString);
            asciiConvertedString = Convert.ToBase64String(byt);
            return asciiConvertedString;
        }
    }
}