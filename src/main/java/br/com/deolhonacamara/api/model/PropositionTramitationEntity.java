package br.com.deolhonacamara.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropositionTramitationEntity {
    private Integer id;
    private Integer propositionId;
    private LocalDateTime dateTime;
    private Integer sequence;
    private String orgAcronym;
    private String orgUri;
    private String lastReporterUri;
    private String regime;
    private String tramitationDescription;
    private String tramitationTypeCode;
    private String situationDescription;
    private String situationCode;
    private String dispatch;
    private String url;
    private String scope;
    private String appreciation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Portuguese-named accessors kept for backward compatibility with existing code
    public LocalDateTime getDataHora() { return this.dateTime; }
    public void setDataHora(LocalDateTime dataHora) { this.dateTime = dataHora; }

    public Integer getSequencia() { return this.sequence; }
    public void setSequencia(Integer sequencia) { this.sequence = sequencia; }

    public String getSiglaOrgao() { return this.orgAcronym; }
    public void setSiglaOrgao(String siglaOrgao) { this.orgAcronym = siglaOrgao; }

    public String getUriOrgao() { return this.orgUri; }
    public void setUriOrgao(String uriOrgao) { this.orgUri = uriOrgao; }

    public String getUriUltimoRelator() { return this.lastReporterUri; }
    public void setUriUltimoRelator(String uriUltimoRelator) { this.lastReporterUri = uriUltimoRelator; }

    public String getRegimePortuguese() { return this.regime; } // not used but explicit

    public String getDescricaoTramitacao() { return this.tramitationDescription; }
    public void setDescricaoTramitacao(String descricaoTramitacao) { this.tramitationDescription = descricaoTramitacao; }

    public String getCodTipoTramitacao() { return this.tramitationTypeCode; }
    public void setCodTipoTramitacao(String codTipoTramitacao) { this.tramitationTypeCode = codTipoTramitacao; }

    public String getDescricaoSituacao() { return this.situationDescription; }
    public void setDescricaoSituacao(String descricaoSituacao) { this.situationDescription = descricaoSituacao; }

    public String getCodSituacao() { return this.situationCode; }
    public void setCodSituacao(String codSituacao) { this.situationCode = codSituacao; }

    public String getDespacho() { return this.dispatch; }
    public void setDespacho(String despacho) { this.dispatch = despacho; }

    public String getAmbito() { return this.scope; }
    public void setAmbito(String ambito) { this.scope = ambito; }

    public String getApreciacao() { return this.appreciation; }
    public void setApreciacao(String apreciacao) { this.appreciation = apreciacao; }

}
