using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;

namespace ISF_WEB.Util
{
    public class EncryptDecryptUtil
    {
        public string Reverse(string value)
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
            asciiConvertedString = asciiConvertedString.Remove(asciiConvertedString.Length - 1);
            byte[] byt = Encoding.UTF8.GetBytes(asciiConvertedString);
            asciiConvertedString = Convert.ToBase64String(byt);
            return asciiConvertedString;
        }

        public string Encryption(string value)
        {
            return ConvertStringToAsciiWithPipe(Reverse(value));
        }

        public string Decryption(string value)
        {

            string ReverseString = string.Empty;
        

                byte[] ByteArray = Convert.FromBase64String(value);

                string Stringarray = Encoding.UTF8.GetString(ByteArray);


                string[] ByteStrings = Stringarray.Split("|".ToCharArray());
                byte[] ByteOut = new byte[ByteStrings.Length];

                for (int i = 0; i <= (ByteStrings.Length - 1); i++)
                    ByteOut[i] = Byte.Parse(ByteStrings[i]);



                ReverseString = Encoding.ASCII.GetString(ByteOut);
           


            return ReverseString;
        }
    }
}