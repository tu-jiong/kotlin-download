package com.example.administrator.kotlindemo

/**
 * Created by Tujiong on 2017/6/2.
 */
open class ParentClass {

    open fun func() {

    }

    /**
     *替代静态变量和方法
     */
    companion object {
        val TAG: String = ParentClass::class.java.simpleName

        fun start() {

        }
    }

    class A(str: String) {

        constructor(str: String, index: Int) : this(str)

        init {

        }

    }

    class B(val str: String) {

        var name: String = ""
            set(value) {
                field = "Jack"
            }
            get() {
                return field + " is me"
            }

        var age: String? = null

        fun testb() {

        }
    }

    class Test {
        val a = A("a")
        val b: B? = null

        fun test() {
            b?.testb()
        }
    }

}