#include <string>
#include <iostream>
#include <vector>
#include <functional> 
#include <cstdarg>  
#include <variant>

using namespace std;

#define BEGIN_GAME int main() {Pokemon p{"init","init",10}; Ability a{"init", nullptr};  function<void(void)> f
#define END_GAME ;}
#define CREATE ;storage += 

// pokemon creation macros
#define POKEMONS storageIn
#define POKEMON p = Pokemon
#define NAME dummy() == true ? "false"
#define TYPE NAME
#define HP dummy() == true ? -1

// ability creation macros
#define ABILITIES storageIn
#define ABILITY a = Ability  
#define ACTION dummy() == true ? []() {cout << "";}
#define START f = []() { string action = "damage"; string target = "DEFENDER"; int value = 0; bool exec = true;
#define END ;if (exec == true) {if (action == "damage") { damage(target, value); } else {heal(target, value);} exec = false; } }
#define DAMAGE action = "damage";
#define HEAL action = "heal";
#define DEFENDER target = "DEFENDER"; value = 
#define ATTACKER target = "ATTACKER"; value =
#define DEAR ;dear(
#define LEARN )->a

#define GET_HP(x) getHp(#x)
#define GET_NAME(x) getName(#x)
#define GET_TYPE(x) getType(#x)
#define ABILITY_NAME(x) abilitiesName(#x)

#define AND(x, ...) variadic(0, x, __VA_ARGS__, -1)
#define OR(x, ...) variadic(1, x, __VA_ARGS__, -1)
#define NOT(x) !x

bool variadic(int fun, int a, ...) {
    bool result = (bool)a;
    va_list args;

    va_start(args, a);
    int y = va_arg(args, int);
    while(y != -1) {
        if (fun == 0) { // 0 -> AND operator
            result &= (bool)y;
        } else {        // 1 -> OR operator
            result |= (bool)y;
        }
        y = va_arg(args, int);
    }
    va_end(args);
    return result;
}

#define IF if (
#define DO ) {
#define ELSE_IF ;} else if ( 
#define ELSE ;} else {

#define FOR for (int i = 0; i < 
#define ROUNDS ; i++

#define SHOW ;cout << 

#define DUEL ;duel();

class Ability {
    private:
        string name;

    public:
        /* function for the action of the ability */
        function<void(void)> functionPtr;

        static vector<Ability> abilityTemp;
        
        void setName(string _name) {name = _name;}
        string getName() {return name;}

        Ability(string name, function<void(void)> functionPtr) : name{name}, functionPtr{functionPtr} {}

        Ability operator,(const Ability newAbility) {
            
            abilityTemp.push_back(newAbility);
            return *this;
        }

        Ability operator+=(const Ability newAbility) {
            
            abilityTemp.push_back(newAbility);
            return *this;
        }
};

vector<Ability> Ability::abilityTemp;

class Pokemon {
    private:
        string name;
        string type;
        int hp;
        bool inPokeball;
        int maxHp;

    public:
        static vector<Pokemon> pokemonTemp;

        bool isInPokeball() {
            return inPokeball;
        }

        int getMaxHp() {
            return maxHp;
        }

        class LearntAbilities {
            public:
                vector<Ability*> learntAbilities;

                LearntAbilities* operator[](Ability* a) {
                    learntAbilities.push_back(a);

                    return this;
                }
        };

        LearntAbilities a;

        Pokemon operator,(const Pokemon f) {
            
            pokemonTemp.push_back(f);
            return *this;
        }

        void setName(string _name) {name = _name;}
        string getName() {return name;}

        void setType(string _type) {type = _type;}
        string getType() {return type;}

        void setHp(int _hp) {hp = _hp;}
        int getHp() {return hp;}

        Pokemon(string name, string type, int hp) : name{name}, type{type}, hp{hp}, maxHp{hp}, inPokeball{false} {}

        void display() {
            cout << "Name: " << name << "\nType: " << type << "\nHp: " << hp << endl;
        }
};

vector<Pokemon> Pokemon::pokemonTemp;

class Player {
    public:
        Pokemon p;
        bool isAttacking;

        void displayAbilities() {
            cout << "-------------------------------\n";
            for (int i = 0; i < p.a.learntAbilities.size(); i++) {
                cout << p.a.learntAbilities[i]->getName() << endl;
            }
            cout << "-------------------------------\n";
        }

        Player(Pokemon p, bool b) : p{p}, isAttacking{b} {} 
};

bool dummy() {
    return false;
}

class Storage {
    public:
        vector<Pokemon> pokemons;
        vector<Ability> abilities;

        Storage operator+=(Pokemon p) {
            pokemons.push_back(p);

            return *this;
        }

        Storage operator+=(Ability a) {
            abilities.push_back(a);

            return *this;
        }

        Storage operator+=(Storage z) {
            int f = 0;
            for (int i = 0; i < Pokemon::pokemonTemp.size(); i++) {
                f = 0;
                for (int j = 0; j < pokemons.size(); j++) {
                    if (pokemons[j].getName() == Pokemon::pokemonTemp[i].getName()) {
                        f = 1;
                        break;
                    }
                }
                if (f == 0) {
                    pokemons.push_back(Pokemon::pokemonTemp[i]);
                }
            }

            for (int i = 0; i < Ability::abilityTemp.size(); i++) {
                f = 0;
                for (int j = 0; j < abilities.size(); j++) {
                    if (abilities[j].getName() == Ability::abilityTemp[i].getName()) {
                        continue;
                    }
                }
                if (f == 0) {
                    abilities.push_back(Ability::abilityTemp[i]);
                }
            }
            return *this;
        }

        Storage operator[](Pokemon p) {
            Pokemon::pokemonTemp.push_back(p);

            return *this;
        }

        Storage operator[](Ability a) {
            Ability::abilityTemp.push_back(a);

            return *this;
        }
};
  
Player *players[2]; 
Storage storage, storageIn;
int currentRound;

Player* getPlayer(int index) {
    return players[index];
}

Player* getTarget(string target) {
    if (target == "DEFENDER") {
        for (int i = 0; i < 2; i++) {
            if (players[i]->isAttacking == false) {
                return players[i];
            }
        }
    } else if (target == "ATTACKER") {
        for (int i = 0; i < 2; i++) {
            if (players[i]->isAttacking == true) {
                return players[i];
            }
        }
    }

    return nullptr;
}
 
int getHp(string target) {
    return getTarget(target)->p.getHp();
}

string getType(string target) {
    return getTarget(target)->p.getType();
}

string getName(string target) {
    return getTarget(target)->p.getName();
}

// searches in pokemons[] by name and returns it
Pokemon* dear(string name) {
    for (int i = 0; i < storage.pokemons.size(); i++) {
        if(storage.pokemons[i].getName() == name) {
            return &storage.pokemons[i];
        }
    }

    return nullptr;
}

// searches in abilities[] by name and returns itstorage.pokemons.size()
Ability* abilitiesName(string name) {
    for (int i = 0; i < storage.abilities.size(); i++) {
        if (storage.abilities[i].getName() == name) {
            return &storage.abilities[i];
        }
    }

    return nullptr;
} 

void displayPokemons() {
    cout << "-------------------------------\n";
    for (int i = 0; i < storage.pokemons.size(); i++) {
        cout << storage.pokemons[i].getName() << endl;
    }
    cout << "-------------------------------\n";
}

float damageDealtAmp() {
    string attackerType = getType("ATTACKER");
    string defenderType = getType("DEFENDER");
    
    float amp = 1; 

    if (attackerType == "Fire") {
        if (defenderType == "Electric") {
            amp += 0.2;
        } else {
            amp += 0.15;
        }
    } else if (attackerType == "Water") {
        amp += 0.07;
    } else if (attackerType == "Grass" && currentRound%2 == 1) {
        amp += 0.07;
    }
    return amp;
}

float damageReceivedAmp() {
    string attackerType = getType("ATTACKER");
    string defenderType = getType("DEFENDER");

    float amp = 1; 

    if (defenderType == "Electric") {
        if (attackerType == "Fire") {
            amp -= 0.3;
        } else {
            amp -= 0.2;
        }
    } else if (defenderType == "Water") {
        amp -= 0.07;
    }
    return amp;
}


void damage(string target, int value) {
    Player* player = getTarget(target);
    value = (int) (value * damageDealtAmp() * damageReceivedAmp());

    int hp = player->p.getHp();
    player->p.setHp(hp - value);
}

void heal(string target, int value) {
    Player* player = getTarget(target);
    int hp = player->p.getHp();
    
    if ((hp + value) > player->p.getMaxHp()) {
        player->p.setHp(player->p.getMaxHp());
    } else {
        player->p.setHp(player->p.getHp() + value);
    }
}

void check() {
    for (auto item : storage.pokemons) {
        if (item.getType() != "Grass" && item.getType() != "Fire" && item.getType() != "Electric" && item.getType() != "Water") {
            cerr << "Pokemon type has to be one of the following: Grass - Fire - Electric - Water" << endl;
            exit(1);
        }

        if (item.getHp() <= 0) {
            cerr << "Pokemon hp has to be an integer > 0." << endl;
            exit(1);
        }
    }    

    for (auto item : storage.abilities) {
        if (item.getName().find(" ") != string::npos) {
            cerr << "Ability name cannot contain whitespace." << endl;
            exit(1);
        }
    }    
}

void duel() {
    check();
    cout << "---------------------------------------------------------------------------------------------\n";
    cout << "  ____  ____  _  __ _____ _      ____  _        _____  _     _____   _____ ____  _      _____" << endl;
    cout << " /  __\\/  _ \\/ |/ //  __// \\__/|/  _ \\/ \\  /|  /__ __\\/ \\ /|/  __/  /  __//  _ \\/ \\__/|/  __/"<< endl;
    cout << " |  \\/|| / \\||   / |  \\  | |\\/||| / \\|| |\\ ||    / \\  | |_|||  \\    | |  _| / \\|| |\\/|||  \\  "<< endl;
    cout << " |  __/| \\_/||   \\ |  /_ | |  ||| \\_/|| | \\||    | |  | | |||  /_   | |_//| |-||| |  |||  /_ "<< endl;
    cout << " \\_/   \\____/\\_|\\_\\\\____/\\_/  \\|\\____/\\_/  \\|    \\_/  \\_/ \\|\\____\\  \\____/ \\_/ \\|\\_/  \\|\\____\\"<< endl;
    cout << "\n---------------------------------------------------------------------------------------------\n";

    for (int i = 0; i < 2; i++) {
        string name;
        do {
            cout << "\nPlayer" << i+1 << " select pokemon:\n";
            displayPokemons();
            getline(cin, name);
        } while(dear(name) == nullptr);
        players[i] = new Player{*dear(name), (bool)i}; 
    } 
    Player* attacker;
    currentRound = 1;
    int p = 0;
    while(getPlayer(0)->p.getHp() > 0 && getPlayer(1)->p.getHp() > 0){
        attacker = players[p];
        players[p]->isAttacking = true;
        players[(p+1)%2]->isAttacking = false;
        if(p==0) {
            cout << "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\nRound "<< currentRound << "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
            if (currentRound%2 == 0) {
                for (int i = 0; i < 2; i++) {
                    if (players[i]->p.getType() == "Grass") {
                        heal((players[i]->isAttacking == true ? "ATTACKER" : "DEFENDER") , (int) (players[i]->p.getMaxHp()*0.05));
                    }
                }
            }
            currentRound++;
        }
        string pokemonName = attacker->p.getName();
        string abilityName;
        Ability* abilityToExec;
        if(attacker->p.isInPokeball() == false){
            do {
                cout << "\n" << pokemonName << "(Player" << p+1 << ") select ability:\n";
                attacker->displayAbilities();
                cin >> abilityName;
            } while((abilityToExec = abilitiesName(abilityName)) == nullptr);

            abilityToExec->functionPtr();
            for(int i=0; i<2; i++){
                pokemonName = getPlayer(i)->p.getName();
                int pokemonHP = getPlayer(i)->p.getHp();
                bool pokemonIsInPokeball = getPlayer(i)->p.isInPokeball();
                if(pokemonHP < 0 ){
                    pokemonHP = 0;
                }
                cout<< "\n\n##############################\nName: "<< pokemonName << "\nHP: " << pokemonHP << "\nPokemon";
                if(pokemonIsInPokeball == true){
                    cout<< " in Pokeball";
                }else{
                    cout<< " out of Pokeball";
                }
                cout<<"\n##############################\n\n";
            }
        }else{
            cout << "\n" << pokemonName << "(Player" << p+1 << ") has not a pokemon out of pokeball so he can't cast an ability.\n";
        }
        p++;
        p %= 2;
    }

    string winner;
    if(getPlayer(0)->p.getHp() > 0) {
        winner = "Player1 won!";
    } else {
        winner = "Player2 won!";
    }
    cout<<"                  .-=========-." << endl;
    cout<<"                  \\'-=======-'/"<< endl;
    cout<<"                  _|   .=.   |_" << endl;
    cout<<"                 ((|  {{1}}  |))"<< endl;
    cout<< winner << "      \\|   /|\\   |/"<< endl;
    cout<<"                   \\__ ''' __/" << endl;
    cout<<"                     _') ('_"    << endl;
    cout<<"                   _/_______\\_" << endl;
    cout<<"                  /___________\\"<< endl;
}