#include "PokemonLeague.h"

BEGIN_GAME

    CREATE POKEMON {
        NAME: "Charmander",
        TYPE: "Fire",
        HP: 100
    }

    CREATE POKEMON {
        NAME: "Squirtle",
        TYPE: "Water",
        HP: 100
    }

    CREATE POKEMONS [
        POKEMON{
            NAME: "Pikachu",
            TYPE: "Grass",
            HP: 100
        },
        POKEMON{
            NAME: "Charizard",
            TYPE: "Fire",
            HP: 100
        }, 
        POKEMON{
            NAME: "Bulbasaur",
            TYPE: "Grass",
            HP: 100
        },
        POKEMON{
            NAME: "Bidoof",
            TYPE: "Electric",
            HP: 100
        }
    ]

    CREATE ABILITY {
        NAME: "Rain_Dish",
        ACTION: START
            IF  OR (GET_TYPE(ATTACKER) == "Electric", GET_HP(ATTACKER) < 20, GET_HP(DEFENDER) < 90) DO
                HEAL ATTACKER 30
            END
        END
    }

    CREATE ABILITY {
        NAME: "Drosught",
        ACTION: START
            IF  NOT (AND (GET_HP(DEFENDER) > 20, GET_HP(DEFENDER) < 70)) DO
                DAMAGE ATTACKER 15
            END
        END
    }


    CREATE ABILITIES [
        ABILITY {
            NAME: "Shock",
            ACTION: START
                IF GET_HP(DEFENDER) <= 90 DO 
                    DAMAGE DEFENDER 30
                ELSE 
                    DAMAGE DEFENDER 50
                END
            END
        },
        ABILITY {
            NAME: "Blaze",
            ACTION: START
                IF AND (GET_TYPE(ATTACKER) == "Grass", GET_HP(ATTACKER) > 20, GET_HP(DEFENDER) > 10) DO
                    DAMAGE DEFENDER 10
                END
            END
        }
    ]

    DEAR "Pikachu" LEARN [
        ABILITY_NAME(Shock)
    ]

    DEAR "Pikachu" LEARN [
        ABILITY_NAME(Blaze)
    ]

    DEAR "Charizard" LEARN [
        ABILITY_NAME(Blaze)
    ]

    DEAR "Charizard" LEARN [
        ABILITY_NAME(Drought)
    ]

    DEAR "Charmander" LEARN [
        ABILITY_NAME(Drought)
    ]

    DEAR "Charmander" LEARN [
        ABILITY_NAME(Blaze)
    ]

    DEAR "Bidoof" LEARN [
        ABILITY_NAME(Rain_Dish)
    ]

    DEAR "Bidoof" LEARN [
        ABILITY_NAME(Shock)
    ]

    DEAR "Bidoof" LEARN [
        ABILITY_NAME(Blaze)
    ]

    DEAR "Squirtle" LEARN [
        ABILITY_NAME(Rain_Dish)
    ]

    DEAR "Squirtle" LEARN [
        ABILITY_NAME(Blaze)
    ]

    DEAR "Bulbasaur" LEARN [
        ABILITY_NAME(Drought)
    ]

    DEAR "Bulbasaur" LEARN [
        ABILITY_NAME(Rain_Dish)
    ]

    DUEL

END_GAME