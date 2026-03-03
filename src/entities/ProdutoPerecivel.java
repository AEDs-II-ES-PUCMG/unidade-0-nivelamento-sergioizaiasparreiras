import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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
        long diasValidade = ChronoUnit.DAYS.between(LocalDate.now(), this.dataDeValidade);

        if(diasValidade <= PRAZO_DESCONTO){
            desconto = DESCONTO;
        }

        return (this.precoCusto * (1 + this.margemLucro)) * (1 - desconto);
    }

    @Override
    public String gerarDadosTexto() {
        DateTimeFormatter frm = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format(java.util.Locale.US, "2;%s;%.2f;%.2f;%s", this.descricao, this.precoCusto, this.margemLucro, frm.format(this.dataDeValidade));
    }
    
    @Override
    public String toString() {
       DateTimeFormatter frm = DateTimeFormatter.ofPattern("dd/MM/yyyy");
       String dados = super.toString();
       dados += "\nValidade até " + frm.format(this.dataDeValidade);
       return dados;
    }
}