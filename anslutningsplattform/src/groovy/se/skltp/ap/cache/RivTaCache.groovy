package se.skltp.ap.cache

interface RivTaCache {
    List<RivTaTjansteDoman> getDomaner()
    List<RivTaTjansteKontrakt> getTjansteKontraktForDoman(String domanId)

}
