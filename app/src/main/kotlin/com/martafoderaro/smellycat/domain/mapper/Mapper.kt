package com.martafoderaro.smellycat.domain.mapper

interface Mapper<OUT, IN> {
    fun map(input: IN): OUT

    fun map(input: List<IN>): List<OUT>
}