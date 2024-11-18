#include "symtable.h"

void print(const char *pcKey, void *pvValue,void *pvExtra)
{
    printf("Entry %s -> %s\n",pcKey,(char *)pvValue);
}

int main()
{
    SymTable_T onomata = SymTable_new();
    int i;
    char result[50];

    if(SymTable_put(onomata,"maria","123"))
      {
        printf("You just added one element\n");
      }
    else
      {
        printf("SymTable_put has problem\n");
      }

    if(!SymTable_put(onomata,"maria","123"))
      {
        printf("It works,it did not add second instance\n");
      }
    else
      {
        printf("SymTable_put dublicates an entry\n");
      }

    if(SymTable_put(onomata,"stavros","987"))
      {
        printf("You just added a second element\n");
      }
    else
      {
        printf("SymTable_put problem\n");
      }

    if(SymTable_put(onomata,"kostas","456"))
      {
        printf("You just added a thrid element\n");
      }
      else
      {
        printf("Problem at SymTable_put");
      }

    if(SymTable_remove(onomata,"stavros"))
      {
        printf("removed 2nd element \n");
      }
    else
      {
        printf("SymTable_remove failed\n");
      }


    for(i=0;i<500;i++)
    {
        sprintf(result, "name%d", i);
        SymTable_put(onomata, result ,"value");
    }

    if(SymTable_remove(onomata,"name85"))
      {
          printf("Removed name85 element\n");
      }
      else
        {
          printf("SymTable_remove failed\n");
        }

    if(!SymTable_get(onomata,"name85"))
      {
        printf("Removed name85 element\n");
      }
    else
      {
        printf("SymTable_remove function failed\n");
      }

    if(SymTable_get(onomata, "name55"))
      {
        printf("Got name55: %s element\n",(char*)SymTable_get(onomata, "name55"));
      }
    else
      {
        printf("SymTable_get function failed\n");
      }




    SymTable_free(onomata);

     return 0;

}