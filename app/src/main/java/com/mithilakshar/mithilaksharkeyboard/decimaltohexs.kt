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


        //
        73593 to "\uD805\uDC99", // 𑒙
        73594 to "\uD805\uDC9A", // 𑒚
        73595 to "\uD805\uDC9B", // 𑒛
        73596 to "\uD805\uDC9C", // 𑒜
        73597 to "\uD805\uDC9D", // 𑒝

        73598 to "\uD805\uDC99", // 𑒞
        73599 to "\uD805\uDC9A", // 𑒟
        73600 to "\uD805\uDC9B", // 𑒠
        73601 to "\uD805\uDC9C", // 𑒡
        73602 to "\uD805\uDC9D", // 𑒢

        73598 to "\uD805\uDC99", // 𑒣
        73599 to "\uD805\uDC9A", // 𑒤
        73600 to "\uD805\uDC9B", // 𑒥
        73601 to "\uD805\uDC9C", // 𑒦
        73602 to "\uD805\uDC9D", // 𑒧

        73598 to "\uD805\uDC99", // 𑒨
        73599 to "\uD805\uDC9A", // 𑒩
        73600 to "\uD805\uDC9B", // 𑒪
        73601 to "\uD805\uDC9C", // 𑒫
        73602 to "\uD805\uDC9D", // 𑒬

        73598 to "\uD805\uDC99", // 𑒭
        73599 to "\uD805\uDC9A", // 𑒮
        73600 to "\uD805\uDC9B", // 𑒯


        73703 to  "\uD805\uDCC7", //𑓇
        128054 to  "\uD83D\uDC36", //🐶
        -44 to  "", //🐶
        73406 to "\uD805\uDCBE", //𑒾

        //matra
        73408 to "\uD805\uDCB0", // 𑒰
        73409 to "\uD805\uDCB1", // 𑒱
        73410 to "\uD805\uDCB2", // 𑒲
        73411 to "\uD805\uDCB3", // 𑒳
        73412 to "\uD805\uDCB4", // 𑒴
        73413 to "\uD805\uDCB5", // 𑒵
        73414 to "\uD805\uDCB6", // 𑒶
        73415 to "\uD805\uDCB7", // 𑒷
        73416 to "\uD805\uDCB8", // 𑒸
        73417 to "\uD805\uDCB9", // 𑒹
        73418 to "\uD805\uDCBA", // 𑒺
        73419 to "\uD805\uDCBB", // 𑒻
        73420 to "\uD805\uDCBC", // 𑒼
        73421 to "\uD805\uDCBD", // 𑒽
        73422 to "\uD805\uDCBE"  ,// 𑒾

        73408 to "\uD805\uDCB0", //  𑒿
        73408 to "\uD805\uDCB0", // 𑓀
        73408 to "\uD805\uDCB0", // 𑓁
        73408 to "\uD805\uDCB0", // 𑓂
        73408 to "\uD805\uDCB0", // 𑓃
        73408 to "\uD805\uDCB0", // 𑓄
        73408 to "\uD805\uDCB0", // 𑓅
        73408 to "\uD805\uDCB0", // 𑓆



        // Vowels
        2309 to "\uD805\uDC80", // अ (U+0905) to 𑑀 (U+11480)
        2310 to "\uD805\uDC81", // आ (U+0906) to 𑑁 (U+11481)
        2311 to "\uD805\uDC82", // इ (U+0907) to 𑑂 (U+11482)
        2312 to "\uD805\uDC83", // ई (U+0908) to 𑑃 (U+11483)
        2313 to "\uD805\uDC84", // उ (U+0909) to 𑑄 (U+11484)
        2314 to "\uD805\uDC85", // ऊ (U+090A) to 𑑅 (U+11485)
        2315 to "\uD805\uDC86", // ऋ (U+090B) to 𑑆 (U+11486)
        2316 to "\uD805\uDC87", // ॠ (U+090C) to 𑑇 (U+11487)
        2317 to "\uD805\uDC88", // ऌ (U+090D) to 𑑈 (U+11488)
        2318 to "\uD805\uDC89", // ॡ (U+090F) to 𑑉 (U+11489)

        // Consonants
        2325 to "\uD805\uDC8A", // क (U+0915) to 𑒊 (U+1148A)
        2326 to "\uD805\uDC8B", // ख (U+0916) to 𑒋 (U+1148B)
        2327 to "\uD805\uDC8C", // ग (U+0917) to 𑒌 (U+1148C)
        2328 to "\uD805\uDC8D", // घ (U+0918) to 𑒍 (U+1148D)
        2329 to "\uD805\uDC8E", // ङ (U+0919) to 𑒎 (U+1148E)

        2330 to "\uD805\uDC8F", // च (U+091A) to 𑒏 (U+1148F)
        2331 to "\uD805\uDC90", // छ (U+091B) to 𑒐 (U+11490)
        2332 to "\uD805\uDC91", // ज (U+091C) to 𑒑 (U+11491)
        2333 to "\uD805\uDC92", // झ (U+091D) to 𑒒 (U+11492)
        2334 to "\uD805\uDC93", // ञ (U+091E) to 𑒓 (U+11493)

        2335 to "\uD805\uDC94", // ट (U+091F) to 𑒔 (U+11494)
        2336 to "\uD805\uDC95", // ठ (U+0920) to 𑒕 (U+11495)
        2337 to "\uD805\uDC96", // ड (U+0921) to 𑒖 (U+11496)
        2338 to "\uD805\uDC97", // ढ (U+0922) to 𑒗 (U+11497)
        2339 to "\uD805\uDC98", // ण (U+0923) to 𑒘 (U+11498)

        2340 to "\uD805\uDC99", // त (U+0924) to 𑒙 (U+11499)
        2341 to "\uD805\uDC9A", // थ (U+0925) to 𑒚 (U+1149A)
        2342 to "\uD805\uDC9B", // द (U+0926) to 𑒛 (U+1149B)
        2343 to "\uD805\uDC9C", // ध (U+0927) to 𑒜 (U+1149C)
        2344 to "\uD805\uDC9D", // न (U+0928) to 𑒝 (U+1149D)

        2345 to "\uD805\uDCA0", // प (U+092A) to 𑒠 (U+114A0)
        2346 to "\uD805\uDCA1", // फ (U+092B) to 𑒡 (U+114A1)
        2347 to "\uD805\uDCA2", // ब (U+092C) to 𑒢 (U+114A2)
        2348 to "\uD805\uDCA3", // भ (U+092D) to 𑒣 (U+114A3)
        2349 to "\uD805\uDCA4", // म (U+092E) to 𑒤 (U+114A4)

        2350 to "\uD805\uDCA5", // य (U+092F) to 𑒥 (U+114A5)
        2351 to "\uD805\uDCA6", // र (U+0930) to 𑒦 (U+114A6)
        2352 to "\uD805\uDCA7", // ल (U+0932) to 𑒧 (U+114A7)
        2353 to "\uD805\uDCA8", // व (U+0935) to 𑒨 (U+114A8)
        2354 to "\uD805\uDCA9", // श (U+0937) to 𑒩 (U+114A9)

        2355 to "\uD805\uDCAA", // ष (U+0937) to 𑒪 (U+114AA)
        2356 to "\uD805\uDCAB", // स (U+0938) to 𑒫 (U+114AB)
        2357 to "\uD805\uDCAC", // ह (U+0939) to 𑒬 (U+114AC)

        // Additional characters
        2309 to "\uD805\uDCC0", // अं (U+0905 U+0902) to 𑑀 (U+11480)
        2357 to "\uD805\uDCC1",  // अः (U+0905 U+0939) to 𑑁 (U+11481)

        // Lowercase English letters
        97 to "\uD805\uDC81", // 'a'
        98 to "\uD805\uDCB0", // 'b'
        99 to "\uD805\uDC91", // 'c'
        100 to "\uD805\uDC92", // 'd'
        101 to "\uD805\uDC93", // 'e'
        102 to "\uD805\uDC94", // 'f'
        103 to "\uD805\uDC95", // 'g'
        104 to "\uD805\uDC96", // 'h'
        105 to "\uD805\uDC97", // 'i'
        106 to "\uD805\uDC98", // 'j'
        107 to "\uD805\uDC99", // 'k'
        108 to "\uD805\uDC9A", // 'l'
        109 to "\uD805\uDC9B", // 'm'
        110 to "\uD805\uDC9C", // 'n'
        111 to "\uD805\uDC9D", // 'o'
        112 to "\uD805\uDC9E", // 'p'
        113 to "\uD805\uDC9F", // 'q'
        114 to "\uD805\uDCA0", // 'r'
        115 to "\uD805\uDCA1", // 's'
        116 to "\uD805\uDCA2", // 't'
        117 to "\uD805\uDCA3", // 'u'
        118 to "\uD805\uDCA4", // 'v'
        119 to "\uD805\uDCA5", // 'w'
        120 to "\uD805\uDCA6", // 'x'
        121 to "\uD805\uDCA7", // 'y'
        122 to "\uD805\uDCA8", // 'z'

        // Uppercase English letters
        65 to "\uD805\uDC8F", // 'A'
        66 to "\uD805\uDC90", // 'B'
        67 to "\uD805\uDC91", // 'C'
        68 to "\uD805\uDC92", // 'D'
        69 to "\uD805\uDC93", // 'E'
        70 to "\uD805\uDC94", // 'F'
        71 to "\uD805\uDC95", // 'G'
        72 to "\uD805\uDC96", // 'H'
        73 to "\uD805\uDC97", // 'I'
        74 to "\uD805\uDC98", // 'J'
        75 to "\uD805\uDC99", // 'K'
        76 to "\uD805\uDC9A", // 'L'
        77 to "\uD805\uDC9B", // 'M'
        78 to "\uD805\uDC9C", // 'N'
        79 to "\uD805\uDC9D", // 'O'
        80 to "\uD805\uDC9E", // 'P'
        81 to "\uD805\uDC9F", // 'Q'
        82 to "\uD805\uDCA0", // 'R'
        83 to "\uD805\uDCA1", // 'S'
        84 to "\uD805\uDCA2", // 'T'
        85 to "\uD805\uDCA3", // 'U'
        86 to "\uD805\uDCA4", // 'V'
        87 to "\uD805\uDCA5", // 'W'
        88 to "\uD805\uDCA6", // 'X'
        89 to "\uD805\uDCA7", // 'Y'
        90 to "\uD805\uDCA8"  // 'Z'


        )

    fun getdecimaltohexsurrogate(code: Int): String? {
        return surrogatePairMap[code]
    }

    private val englishToTirhutaMap = mapOf(
        // Lowercase English letters to Tirhuta characters
        'a' to "\uD805\uDC8F", // 𑒏
        'b' to "\uD805\uDC90", // 𑒐
        'c' to "\uD805\uDC91", // 𑒑
        'd' to "\uD805\uDC92", // 𑒒
        'e' to "\uD805\uDC93", // 𑒓
        'f' to "\uD805\uDC94", // 𑒔
        'g' to "\uD805\uDC95", // 𑒕
        'h' to "\uD805\uDC96", // 𑒖
        'i' to "\uD805\uDC97", // 𑒗
        'j' to "\uD805\uDC98", // 𑒘
        'k' to "\uD805\uDC99", // 𑒙
        'l' to "\uD805\uDC9A", // 𑒚
        'm' to "\uD805\uDC9B", // 𑒛
        'n' to "\uD805\uDC9C", // 𑒜
        'o' to "\uD805\uDC9D", // 𑒝
        'p' to "\uD805\uDC9E", // 𑒞
        'q' to "\uD805\uDC9F", // 𑒟
        'r' to "\uD805\uDCA0", // 𑒠
        's' to "\uD805\uDCA1", // 𑒡
        't' to "\uD805\uDCA2", // 𑒢
        'u' to "\uD805\uDCA3", // 𑒣
        'v' to "\uD805\uDCA4", // 𑒤
        'w' to "\uD805\uDCA5", // 𑒥
        'x' to "\uD805\uDCA6", // 𑒦
        'y' to "\uD805\uDCA7", // 𑒧
        'z' to "\uD805\uDCA8", // 𑒨

        // Uppercase English letters to Tirhuta characters
        'A' to "\uD805\uDC8F", // 𑒏
        'B' to "\uD805\uDC90", // 𑒐
        'C' to "\uD805\uDC91", // 𑒑
        'D' to "\uD805\uDC92", // 𑒒
        'E' to "\uD805\uDC93", // 𑒓
        'F' to "\uD805\uDC94", // 𑒔
        'G' to "\uD805\uDC95", // 𑒕
        'H' to "\uD805\uDC96", // 𑒖
        'I' to "\uD805\uDC97", // 𑒗
        'J' to "\uD805\uDC98", // 𑒘
        'K' to "\uD805\uDC99", // 𑒙
        'L' to "\uD805\uDC9A", // 𑒚
        'M' to "\uD805\uDC9B", // 𑒛
        'N' to "\uD805\uDC9C", // 𑒜
        'O' to "\uD805\uDC9D", // 𑒝
        'P' to "\uD805\uDC9E", // 𑒞
        'Q' to "\uD805\uDC9F", // 𑒟
        'R' to "\uD805\uDCA0", // 𑒠
        'S' to "\uD805\uDCA1", // 𑒡
        'T' to "\uD805\uDCA2", // 𑒢
        'U' to "\uD805\uDCA3", // 𑒣
        'V' to "\uD805\uDCA4", // 𑒤
        'W' to "\uD805\uDCA5", // 𑒥
        'X' to "\uD805\uDCA6", // 𑒦
        'Y' to "\uD805\uDCA7", // 𑒧
        'Z' to "\uD805\uDCA8"  // 𑒨
    )
    fun getTirhutaCharacterFromEnglish(char: Char): String {
        return englishToTirhutaMap[char] ?: "Unknown Character"
    }









}