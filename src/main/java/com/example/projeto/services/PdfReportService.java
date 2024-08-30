package com.example.projeto.services;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projeto.model.Pessoa;
import com.example.projeto.model.Produto;
import com.example.projeto.reposiories.PessoaRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

@Service
public class PdfReportService {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private PessoaRepository pessoaRepository;

    public void generateReport(String dest) throws FileNotFoundException {

        // 1. Criar um escritor de PDF
        PdfWriter writer = new PdfWriter(dest);

        // 2. Criar um documento PDF
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 3. Adicionar um título ao relatório
        Paragraph titulo = new Paragraph("Relatório de Produtos")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(16);
        document.add(titulo);

        
        document.add(new Paragraph("\n"));

        // 4. Criar uma tabela com colunas para os produtos e adicionar cabeçalhos
        float[] columnWidths = { 1, 3, 2, 1, 1 }; 
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(new Cell().add(new Paragraph("Produto")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Descrição")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("CodBarras")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Quantidade")).setBold());
        table.addHeaderCell(new Cell().add(new Paragraph("Preço")).setBold());

        // 5. Adicionar dados à tabela de produtos e calcular o valor total
        List<Produto> listproduto = produtoService.findAll();
        double valorTotal = 0.0;
        for (Produto produto : listproduto) {
            table.addCell(new Cell().add(new Paragraph(produto.getNome())));
            table.addCell(new Cell().add(new Paragraph(produto.getDescricao())));
            table.addCell(new Cell().add(new Paragraph(produto.getCodBarras())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(produto.getQuantidade()))));
            table.addCell(new Cell().add(new Paragraph(String.format("R$ %.2f", produto.getPreco()))));
            valorTotal += produto.getPreco() * produto.getQuantidade(); // Calcular o valor total
        }

        // Adicionar a tabela ao documento
        document.add(table);

        // 6. Adicionar o valor total dos produtos cadastrados
        Paragraph valorTotalParagraph = new Paragraph(String.format("Valor Total em Produtos Cadastrados: R$ %.2f", valorTotal))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBold();
        document.add(valorTotalParagraph);

        
        document.add(new Paragraph("\n\n"));

        // 7. Adicionar uma seção para Pessoas Cadastradas
        Paragraph tituloPessoas = new Paragraph("Clientes Cadastrados")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(16);
        document.add(tituloPessoas);

        
        document.add(new Paragraph("\n"));

        Table pessoasTable = new Table(UnitValue.createPercentArray(new float[]{ 1, 3, 3 })); // Ajuste das colunas
        pessoasTable.setWidth(UnitValue.createPercentValue(100));

        pessoasTable.addHeaderCell(new Cell().add(new Paragraph("ID")).setBold());
        pessoasTable.addHeaderCell(new Cell().add(new Paragraph("Nome")).setBold());
        pessoasTable.addHeaderCell(new Cell().add(new Paragraph("Email")).setBold());

        List<Pessoa> listaPessoas = pessoaRepository.findAll();
        for (Pessoa pessoa : listaPessoas) {
            pessoasTable.addCell(new Cell().add(new Paragraph(String.valueOf(pessoa.getId()))));
            pessoasTable.addCell(new Cell().add(new Paragraph(pessoa.getNome())));
            pessoasTable.addCell(new Cell().add(new Paragraph(pessoa.getEmail())));
        }

        document.add(pessoasTable);

        
        document.close();
    }
}
