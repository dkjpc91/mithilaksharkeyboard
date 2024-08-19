package com.mithilakshar.mithilaksharkeyboard

class decimaltohexs {

    private val surrogatePairMap = mapOf(
        //numbers
        73808 to "\uD805\uDCD0",  // 𑓐
        73809 to "\uD805\uDCD1",// 𑓑
        73810 to "\uD805\uDCD2",// 𑓒
        73811 to "\uD805\uDCD3",// 𑓓
        73812 to "\uD805\uDCD4", // 𑓔
        73813 to "\uD805\uDCD5",// 𑓕
        73814 to "\uD805\uDCD6",// 𑓖
        73815 to "\uD805\uDCD7", // 𑓗
        73816 to "\uD805\uDCD8",// 𑓘
        73817 to "\uD805\uDCD9", // 𑓙

        //vowels
        73568 to "\uD805\uDC80",//𑒀
        73569 to "\uD805\uDC81", //𑒁
        73570 to "\uD805\uDC82", //𑒂
        73571 to "\uD805\uDC83", //𑒃
        73572 to "\uD805\uDC84", //𑒄
        73573 to "\uD805\uDC85", // 𑒅
        73574 to "\uD805\uDC86", // 𑒆
        73575 to "\uD805\uDC87", // 𑒇
        73576 to "\uD805\uDC88", // 𑒈
        73577 to "\uD805\uDC89", // 𑒉
         73578 to "\uD805\uDC8A", // 𑒊
        73579 to "\uD805\uDC8B", // 𑒋
        73580 to "\uD805\uDC8C", // 𑒌
        73581 to "\uD805\uDC8D", // 𑒍
        73582 to "\uD805\uDC8E", // 𑒎

        //Consonent
        73583 to "\uD805\uDC8F", // 𑒏
        73584 to "\uD805\uDC90", // 𑒐
        73585 to "\uD805\uDC91", // 𑒑
        73586 to "\uD805\uDC92", // 𑒒
        73587 to "\uD805\uDC93", // 𑒓

        73588 to "\uD805\uDC94", // 𑒔
        73589 to "\uD805\uDC95", // 𑒕
        73590 to "\uD805\uDC96", //  𑒖
        73591 to "\uD805\uDC97", //  𑒗
        73592 to "\uD805\uDC98", //  𑒘

        73593 to "\uD805\uDC99", // 𑒙
        73594 to "\uD805\uDC9A", // 𑒚
        73595 to "\uD805\uDC9B", // 𑒛
        73596 to "\uD805\uDC9C", // 𑒜
        73597 to "\uD805\uDC9D", // 𑒝


        73703 to  "\uD805\uDCC7", //𑓇




    )

    fun getdecimaltohexsurrogate(code: Int): String? {
        return surrogatePairMap[code]
    }




    private val hexmap = mapOf(
        70272 to "\u11480",  // Example surrogate pair
     // Example for other characters
        // Add mappings for other key codes here
    )

    fun getdecimaltohex(code: Int): String? {
        return hexmap[code]
    }
}