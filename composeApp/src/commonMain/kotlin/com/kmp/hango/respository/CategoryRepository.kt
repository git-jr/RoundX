package com.kmp.hango.respository

import com.kmp.hango.model.Category

val categorySample =
    listOf(
        Category(
            id = "123",
            title = "GEOGRAFIA",
            description = "Perguntas como: O Brasil é um país da América do Sul? Paris é uma cidade francesa?",
            primaryColor = 0xfffc6305,
            secondaryColor = 0xfffe010b
        ),
        Category(
            id = "124",
            title = "CULTURA POP",
            description = "Perguntas como: Daniel Radcliffe interpretou Harry Potter? Beyoncé é uma cantora americana?",
            primaryColor = 0xfffa05fc,
            secondaryColor = 0xfffe01b0
        ),
        Category(
            id = "125",
            title = "VARIEDADES",
            description = "Perguntas como: O café é uma das bebidas mais consumidas no mundo? Os Jogos Olímpicos ocorrem a cada quatro anos?",
            primaryColor = 0xffffd901,
            secondaryColor = 0xff695801
        ),
        Category(
            id = "126",
            title = "NATUREZA",
            description = "Perguntas como: O urso polar vive no Ártico? As plantas produzem oxigênio?",
            primaryColor = 0xff01fa93,
            secondaryColor = 0xff014508
        ),
        Category(
            id = "127",
            title = "CIÊNCIAS",
            description = "Perguntas como: A água é composta por hidrogênio e oxigênio? A Terra é o terceiro planeta do sistema solar?",
            primaryColor = 0xff01b4fa,
            secondaryColor = 0xff015b6e
        )
    )
