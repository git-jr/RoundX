package com.kmp.hango.respository

val questionSamples =
    listOf(
        // Categoria: GEOGRAFIA (id = "123")
        Question(
            id = "1",
            categoryId = "123",
            content = "O Brasil é um país da América do Sul?",
            image = "",
            answers = true
        ),
        Question(
            id = "2",
            categoryId = "123",
            content = "O Monte Everest está localizado na América do Norte?",
            image = "",
            answers = false
        ),
        Question(
            id = "3",
            categoryId = "123",
            content = "A Austrália é considerada um continente?",
            image = "",
            answers = true
        ),
        Question(
            id = "4",
            categoryId = "123",
            content = "O deserto do Saara está na Europa?",
            image = "",
            answers = false
        ),
        Question(
            id = "5",
            categoryId = "123",
            content = "Paris é a capital da França?",
            image = "",
            answers = true
        ),

        // Categoria: CULTURA POP (id = "124")
        Question(
            id = "6",
            categoryId = "124",
            content = "Daniel Radcliffe interpretou Harry Potter?",
            image = "",
            answers = true
        ),
        Question(
            id = "7",
            categoryId = "124",
            content = "A série 'Friends' foi lançada em 1994?",
            image = "",
            answers = true
        ),
        Question(
            id = "8",
            categoryId = "124",
            content = "O filme 'Titanic' foi dirigido por Christopher Nolan?",
            image = "",
            answers = false
        ),
        Question(
            id = "9",
            categoryId = "124",
            content = "A cantora Beyoncé nasceu nos Estados Unidos?",
            image = "",
            answers = true
        ),
        Question(
            id = "10",
            categoryId = "124",
            content = "O personagem Homem de Ferro é interpretado por Chris Hemsworth?",
            image = "",
            answers = false
        ),

        // Categoria: VARIEDADES (id = "125")
        Question(
            id = "11",
            categoryId = "125",
            content = "O café é uma das bebidas mais consumidas no mundo?",
            image = "",
            answers = true
        ),
        Question(
            id = "12",
            categoryId = "125",
            content = "Os Jogos Olímpicos ocorrem a cada dois anos?",
            image = "",
            answers = false
        ),
        Question(
            id = "13",
            categoryId = "125",
            content = "O futebol é o esporte mais popular do mundo?",
            image = "",
            answers = true
        ),
        Question(
            id = "14",
            categoryId = "125",
            content = "O Brasil já sediou uma Copa do Mundo de futebol?",
            image = "",
            answers = true
        ),
        Question(
            id = "15",
            categoryId = "125",
            content = "A pizza foi inventada nos Estados Unidos?",
            image = "",
            answers = false
        ),

        // Categoria: NATUREZA (id = "126")
        Question(
            id = "16",
            categoryId = "126",
            content = "O urso polar vive no Ártico?",
            image = "",
            answers = true
        ),
        Question(
            id = "17",
            categoryId = "126",
            content = "O oceano Atlântico é o maior oceano do mundo?",
            image = "",
            answers = false
        ),
        Question(
            id = "18",
            categoryId = "126",
            content = "As plantas produzem oxigênio?",
            image = "",
            answers = true
        ),
        Question(
            id = "19",
            categoryId = "126",
            content = "Os golfinhos são mamíferos?",
            image = "",
            answers = true
        ),
        Question(
            id = "20",
            categoryId = "126",
            content = "A floresta amazônica está localizada na África?",
            image = "",
            answers = false
        ),

        // Categoria: CIÊNCIAS (id = "127")
        Question(
            id = "21",
            categoryId = "127",
            content = "A água é composta por hidrogênio e oxigênio?",
            image = "",
            answers = true
        ),
        Question(
            id = "22",
            categoryId = "127",
            content = "A Terra é o terceiro planeta do sistema solar?",
            image = "",
            answers = true
        ),
        Question(
            id = "23",
            categoryId = "127",
            content = "O sol é um planeta?",
            image = "",
            answers = false
        ),
        Question(
            id = "24",
            categoryId = "127",
            content = "Os humanos têm 206 ossos no corpo?",
            image = "",
            answers = true
        ),
        Question(
            id = "25",
            categoryId = "127",
            content = "A eletricidade é uma forma de energia mecânica?",
            image = "",
            answers = false
        )
    )


data class Question(
    val id: String,
    val categoryId: String,
    val content: String,
    val image: String,
    val answers: Boolean
)