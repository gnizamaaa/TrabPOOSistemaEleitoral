public class VotacaoNaoExistenteExpection extends Exception {
    public VotacaoNaoExistenteExpection(String errorMessage) {
        super("VOtacao pedida nao existe: "+errorMessage);
    }
}
