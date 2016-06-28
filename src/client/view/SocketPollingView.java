package client.view;

import classicondivise.ComunicazioneView;
import classicondivise.Vendibile;
import classicondivise.bonus.Bonus;
import classicondivise.carte.CartaPermessoCostruzione;
import interfaccecondivise.InterfacciaView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/*
    codice di un thread che fa polling sul socket. Riceve oridini e chiama metodi sulla view.
    Rende trasparente nel caso di comunicazone socket la ricezione di ordini dal server remoto.
 */
public class SocketPollingView implements Runnable {
    private InterfacciaView view;
    ObjectInputStream ois;
    private volatile boolean running = true;

    public SocketPollingView(InterfacciaView view, ObjectInputStream ois) {
        this.view = view;
        this.ois = ois;
    }

    public void termina(){ //TODO: il chiamante deve chiamare questo metodo per far terminare il thread
        running = false;
    }

    @Override
    public void run() {
        try {
            int idMappa, idGiocatore, punti, monete, numCarte, numAiutanti, posizione, numeroEmporiDisponibili;
            String inputLine, idBalcone, colore1, colore2, colore3, colore4, regione, nomeCittà, messaggio;
            List<String> carte, coloriConsiglieri;
            CartaPermessoCostruzione cartaPermessoCostruzione1, cartaPermessoCostruzione2;
            List<CartaPermessoCostruzione> manoCartePermessoCostruzione;
            List<Integer> idGiocatori;
            ComunicazioneView comunicazioneView;
            Bonus bonus;
            List<Bonus> percorsoNobiltà;
            while (running) {
                try{
                    inputLine = (String) ois.readObject();
                    comunicazioneView = ComunicazioneView.valueOf(inputLine);
                    switch (comunicazioneView) {
                        case SCEGLI_MAPPA:
                            view.scegliMappa();
                            break;
                        case INIZIA_A_GIOCARE:
                            //riceve l'id della mappa scelta dal server
                            idMappa = ois.readInt();
                            view.iniziaAGiocare(idMappa);
                            break;
                        case SET_ID_GIOCATORE:
                            idGiocatore = ois.readInt();
                            view.setIdGiocatore(idGiocatore);
                            break;
                        case GET_ID_GIOCATORE:
                           //ci pensa SocketProxyView a restituire l'IDGiocatore
                            break;
                        case UPDATE_PUNTI_VITTORIA_GIOCATORE:
                            idGiocatore = ois.readInt();
                            punti = ois.readInt();
                            view.updatePuntiVittoriaGiocatore(idGiocatore, punti);
                            break;
                        case UPDATE_BALCONE:
                            idBalcone = (String) ois.readObject();
                            colore1 = (String) ois.readObject();
                            colore2 = (String) ois.readObject();
                            colore3 = (String) ois.readObject();
                            colore4 = (String) ois.readObject();
                            view.updateBalcone(idBalcone, colore1, colore2, colore3, colore4);
                            break;
                        case UPDATE_MONETE:
                            idGiocatore = ois.readInt();
                            monete = ois.readInt();
                            view.updateMonete(idGiocatore, monete);
                            break;
                        case UPDATE_CARTE_POLITICA_AVVERSARI:
                            idGiocatore = ois.readInt();
                            numCarte = ois.readInt();
                            view.updateCartePoliticaAvversari(idGiocatore, numCarte);
                            break;
                        case UPDATE_CARTE_POLITICA_PROPRIE:
                            carte = (List<String>) ois.readObject();
                            view.updateCartePoliticaProprie(carte);
                            break;
                        case UPDATE_CARTE_PERMESSO_REGIONE:
                            regione = (String) ois.readObject();
                            cartaPermessoCostruzione1 = (CartaPermessoCostruzione) ois.readObject();
                            cartaPermessoCostruzione2 = (CartaPermessoCostruzione) ois.readObject();
                            view.updateCartePermessoRegione(regione, cartaPermessoCostruzione1, cartaPermessoCostruzione2);
                            break;
                        case UPDATE_CARTE_PERMESSO_GIOCATORE:
                            idGiocatore = ois.readInt();
                            manoCartePermessoCostruzione = (List<CartaPermessoCostruzione>) ois.readObject();
                            view.updateCartePermessoGiocatore(idGiocatore, manoCartePermessoCostruzione);
                            break;
                        case UPDATE_AIUTANTI:
                            idGiocatore = ois.readInt();
                            numAiutanti = ois.readInt();
                            view.updateAiutanti(idGiocatore, numAiutanti);
                            break;
                        case UPDATE_RISERVA_AIUTANTI:
                            numAiutanti = ois.readInt();
                            view.updateRiservaAiutanti(numAiutanti);
                            break;
                        case UPDATE_RISERVA_CONSIGLIERI:
                            coloriConsiglieri = (List<String>) ois.readObject();
                            view.updateRiservaConsiglieri(coloriConsiglieri);
                            break;
                        case UPDATE_PERCORSO_NOBILTA:
                            idGiocatore = ois.readInt();
                            posizione = ois.readInt();
                            view.updatePercorsoNobiltà(idGiocatore, posizione);
                            break;
                        case UPDATE_BONUS_PERCORSO_NOBILTA:
                            percorsoNobiltà = (List<Bonus>) ois.readObject();
                            view.updateBonusPercorsoNobiltà(percorsoNobiltà);
                            break;
                        case UPDATE_EMPORI_CITTA:
                            nomeCittà = (String) ois.readObject();
                            idGiocatori = (List<Integer>) ois.readObject();
                            view.updateEmporiCittà(nomeCittà, idGiocatori);
                            break;
                        case UPDATE_BONUS_CITTA:
                            bonus = (Bonus) ois.readObject();
                            nomeCittà = (String) ois.readObject();
                            view.updateBonusCittà(nomeCittà, bonus);
                            break;
                        case UPDATE_EMPORI_DISPONIBILI_GIOCATORE:
                            idGiocatore = ois.readInt();
                            numeroEmporiDisponibili = ois.readInt();
                            view.updateEmporiDisponibiliGiocatore(idGiocatore, numeroEmporiDisponibili);
                            break;
                        case UPDATE_POSIZIONE_RE:
                            nomeCittà = (String) ois.readObject();
                            view.updatePosizioneRe(nomeCittà);
                            break;
                        case ESEGUI_TURNO:
                            view.eseguiTurno();
                            break;
                        case FINE_TURNO:
                            view.fineTurno();
                            break;
                        case MOSTRA_MESSAGGIO:
                            messaggio = (String) ois.readObject();
                            view.mostraMessaggio(messaggio);
                            break;
                        case COMPRA:
                            view.compra();
                            break;
                        case VENDI:
                            view.vendi();
                            break;
                        case UPDATE_VETRINA:
                            List<Vendibile> inVendita = (List<Vendibile>) ois.readObject();
                            view.updateVetrinaMarket(inVendita);
                            break;
                        default:
                            break;
                    }
                } catch (ClassNotFoundException exc){
                    exc.printStackTrace();
                }
            }
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }
}
