package classicondivise;

import server.model.NomeRegione;

public enum IdBalcone {
    COSTA, COLLINA, MONTAGNA, RE;
    public NomeRegione toNomeRegione(){
        return NomeRegione.valueOf(this.toString());
    }
}
