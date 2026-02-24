import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProdutoPerecivel extends Produto {

    private static final double DESCONTO = 0.25;
    private static final int PRAZO_DESCONTO = 7;
    private LocalDate dataDeValidade;


    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade){
        super(desc, precoCusto, margemLucro);
        if(validade.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("O produto está vencido");
        }
        this.dataDeValidade = validade;
    }
    @Override
    public double valorDeVenda() {

        double desconto = 0d;
        double diasValidade = LocalDate.now().until(dataDeValidade).getDays();

        if(diasValidade <= PRAZO_DESCONTO){
            desconto = DESCONTO;
        }

        return (precoCusto * (1 + margemLucro)) * (1 - desconto);

    }
    
    @Override
    public String toString() {
       DateTimeFormatter frm = DateTimeFormatter.ofPattern("dd/MM/yyyY");
       String dados = super.toString();
       dados += "\nValidade até " + frm.format(dataDeValidade);
       return dados;
    }

}
