package se.skltp.av.cache

interface RivTaCache {
    List<RivTaTjansteDoman> getDomaner()
    List<RivTaTjansteKontrakt> getTjansteKontraktForDoman(String domanId)

}
