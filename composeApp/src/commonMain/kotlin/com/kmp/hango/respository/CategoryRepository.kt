package com.kmp.hango.respository

import com.kmp.hango.model.Category
import hango.composeapp.generated.resources.Res
import hango.composeapp.generated.resources.ic_geo
import hango.composeapp.generated.resources.ic_his
import hango.composeapp.generated.resources.ic_nat
import hango.composeapp.generated.resources.ic_pop
import hango.composeapp.generated.resources.ic_sci
import hango.composeapp.generated.resources.ic_var

val categorySample =
    listOf(
        Category(
            id = "123",
            title = "GEOGRAFIA",
            description = "Perguntas como: O Brasil é um país da América do Sul? Paris é uma cidade francesa?",
            color = 0xFFffa33a,
            icon = Res.drawable.ic_geo
        ),
        Category(
            id = "124",
            title = "CULTURA POP",
            description = "Perguntas como: Daniel Radcliffe interpretou Harry Potter? Beyoncé é uma cantora americana?",
            color = 0xFFffb7e7,
            icon = Res.drawable.ic_pop
        ),
        Category(
            id = "125",
            title = "VARIEDADES",
            description = "Perguntas como: O café é uma das bebidas mais consumidas no mundo? Os Jogos Olímpicos ocorrem a cada quatro anos?",
            color = 0xFFffd100,
            icon = Res.drawable.ic_var
        ),
        Category(
            id = "126",
            title = "NATUREZA",
            description = "Perguntas como: O urso polar vive no Ártico? As plantas produzem oxigênio?",
            color = 0xFF00ff88,
            icon = Res.drawable.ic_nat
        ),
        Category(
            id = "127",
            title = "CIÊNCIAS",
            description = "Perguntas como: A água é composta por hidrogênio e oxigênio? A Terra é o terceiro planeta do sistema solar?",
            color = 0xFF58b1ff,
            icon = Res.drawable.ic_sci
        ),
        Category(
            id = "128",
            title = "HISTÓRIA",
            description = "Perguntas como: O Brasil foi colonizado por Portugal? A Revolução Francesa ocorreu no século XVIII?",
            color = 0xFFc654ff,
            icon = Res.drawable.ic_his
        )
    )
