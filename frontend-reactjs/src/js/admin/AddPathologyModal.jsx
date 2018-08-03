import React from 'react';

// Librerias
import Autocomplete from 'react-autocomplete';

/**
 * Renderiza el modal con los campos de alta de una patologia
 * @param {string} modalId Id del modal
 * @param {string} action Para saber si se esta agregando o eliminando una patologia
 * @param {*} selectedPathology Si action == 'edit' se utiliza para cargar los datos de la patologia a editar
 * @param {Function} getPathologies Funcion para refrescar la tabla
 * @param {Function} actionModal Funcion para manejar los modals
 */
class AddPathologyModal extends React.Component {
    constructor(props) {
        super(props);

        this.modalId = props.modalId;
        this.getPathologies = props.getPathologies;
        this.actionModal = props.actionModal;
        this.action = props.action;

        this.defaultState = {
            name: '',
            numericPhenotypes: [], // Listado final de fenotipos numericos para esta patologia
            categoricPhenotypes: [], // Listado final de fenotipos categoricos para esta patologia
            phenotypesInput: '', // Valor actual del autocomplete
            phenotypesSuggeretion: [], // Las sugerencias del autocomplete actual
            phenotypeType: 'numeric', // Tipo de patologia a buscar
            adding: false,
            selectedPathology: props.selectedPathology
        };

        this.state = this.defaultState;

        // Bindeo la variable 'this' a los metodos llamados desde la vista
        this.handleChange = this.handleChange.bind(this);
        this.handleKeyPress = this.handleKeyPress.bind(this);
        this.savePathology = this.savePathology.bind(this);
        this.getPhenotypesSuggeretion = this.getPhenotypesSuggeretion.bind(this);
    }

    /**
     * Metodo que se ejecuta cuando cambian los props del 
     * componente
     * @param {*} props Nuevas props
     */
    componentWillReceiveProps(props) {
        this.action = props.action;
        this.setState({
            selectedPathology: props.selectedPathology
        });
    }

    /**
     * Metodo que se ejecuta cuando los inputs cambian.
     * Sirve para refrescar el state
     * @param {Event} e Evento del cambio del input
     */
    handleChange(e) {
        // Verifico que el formulario sea valido
        if (!e.target.validity.valid) {
            return;
        }

        // Modifico
        this.setState({ [e.target.name]: e.target.value });
    }

    /**
     * Elimina el valor con el indice especificado
     * @param {number} index Index del valor a eliminar
     */
    removePhenotype(phenotypeType, index) {
        if (phenotypeType == 'numeric') {
            let numericPhenotypes = this.state.numericPhenotypes;
            numericPhenotypes.splice(index, 1);
            this.setState({ numericPhenotypes });
        } else {
            let categoricPhenotypes = this.state.categoricPhenotypes;
            categoricPhenotypes.splice(index, 1);
            this.setState({ categoricPhenotypes });
        }
    }

    /**
     * Limpia todos los inputs del formulario
     */
    cleanInputs() {
        this.setState(this.defaultState);
    }

    /**
     * Guarda una patologia
     * @param {*} phenotype Fenotipo seleccionad
     */
    selectPhenotype(phenotype) {
        console.log("Entra!", phenotype);
        if (this.state.phenotypeType == 'numeric') {
            let numericPhenotypes = this.state.numericPhenotypes;
            numericPhenotypes.push(phenotype);
            this.setState({ numericPhenotypes });
        } else {
            let categoricPhenotypes = this.state.categoricPhenotypes;
            categoricPhenotypes.push(phenotype);
            this.setState({ categoricPhenotypes });
        }
    }

    /**
     * Ni bien se renderiza el componente pedimos los roles
     * al servidor
     */
    componentDidMount() {
        let self = this;
        // Cada vez que se abra el modal, obtengo los roles
        $('#' + self.modalId).on('show.bs.modal', function() {
            if (self.action == 'edit') {
                // Si estamos editando, cargo los datos de la patologia
                // seleccionado en el formulario
                self.setState({
                    name: self.state.selectedPathology.name,
                    numericPhenotypes: self.state.selectedPathology.numericPhenotypes,
                    categoricPhenotypes: self.state.selectedPathology.categoricPhenotypes
                });
            }
        });

        // Cuando ya esta abierto el modal, hago focus en el 
        // primer input
        $('#' + self.modalId).on('shown.bs.modal', function() {
            $('#pathology-name-input').focus();
        });

        // Cuando se esconde limpio los inputs
        $('#' + self.modalId).on('hidden.bs.modal', function() {
            self.cleanInputs();
        });
    }

    /**
     * Maneja el evento cuando se presiona una tecla en el 
     * formualario
     * @param {Event} e Evento de la presion de la tecla
     */
    handleKeyPress(e) {
        // Si presiona enter hacemos el submit
        if (e.which == 13) {
            this.savePathology();
        }
    }

    /**
     * Hace un request al server para agregar una patologia
     */
    savePathology() {
        let self = this;
        // Si el formulario es invalido no hago nada
        if (!self.isFormValid()) {
            return;
        }

        let phenotypeId = self.action == 'edit' ? self.state.selectedPathology.id : '';

        $.ajax({
            url: 'http://localhost:8080/pathologies/' + phenotypeId,
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            type: self.action == 'add' ? 'PUT' : 'PATCH',
            data: JSON.stringify({
                name: self.state.name,
                numericPhenotypes: self.state.numericPhenotypes.map(phenotype => phenotype.id),
                categoricPhenotypes: self.state.categoricPhenotypes.map(phenotype => phenotype.id)
            })
        }).done(function (jsonReponse, textStatus, jqXHR) {
            self.getPathologies(); // Refresco la tabla
            self.actionModal(self.modalId, 'hide'); // Escondo el modal
        }).fail(function (jqXHR, textStatus, errorThrown) {
            alert("Error al dar de alta la patologia. Intente nuevamente más tarde");
            console.log(jqXHR, textStatus, errorThrown);
        });
    }

    /**
     * Edita un valor
     * @param {Event} e Evento de cambio
     * @param {*} idx Index del valor a editar
     */
    changeValue(e, idx) {
        // Verifico que el formulario sea valido
        if (!e.target.validity.valid) {
            return;
        }

        let phenotypes = this.state.phenotypes;
        phenotypes[idx] = e.target.value;
        this.setState({ phenotypes });
    }

    /**
    * Obtiene los phenotypos
    * @param {Event} e Evento de cambio del input
    */
    getPhenotypesSuggeretion(e) {
        let self = this;
        self.setState({ phenotypesInput: e.target.value }, () => {
            let nameSearched = self.state.phenotypesInput;
            // Solo hago el request cuando ingresa al menos 4 caracteres
            if (nameSearched.length < 3) {
                return;
            }

            let url = self.state.phenotypeType == 'numeric' ? 'http://localhost:8080/numeric-phenotypes/' : 'http://localhost:8080/categoric-phenotypes/';
            
            // Hago el request
            $.ajax({
                url: url,
                data: {
                    search: nameSearched
                }
            }).done(function (jsonResponse) {
                // Actualizo la lista de sugerencias
                self.setState({ phenotypesSuggeretion: jsonResponse.content });
            }).fail(function (jqXHR, textStatus, errorThrown) {
                console.log("Error al obtener los nombres", jqXHR, textStatus, errorThrown);
            });;
        });
    }

    /**
     * Valida el formulario
     * @returns True si el formulario es valido, false caso contrario
     */
    isFormValid() {
        return !this.state.adding
                && this.state.name
                && (this.state.numericPhenotypes.length > 0 || this.state.categoricPhenotypes.length > 0);
    }

    render() {
        // Lista con los fenotipos seleccionados
        let valuesComponent = null;

        let numericPhenotypesvaluesList = this.state.numericPhenotypes.map((phenotype, idx) => {
            let deleteButton = <button className="btn btn-danger" onClick={() => this.removePhenotype('numeric', idx)} title="Eliminar valor">-</button>;

            return (
                <div key={"input-value-numeric-div-" + phenotype.id} className="row margin-bottom text-center">
                    <div className="col">
                        {deleteButton}
                    </div>
                    <div className="col">
                        <h5>{phenotype.name}</h5>
                    </div>
                </div>
            );
        });

        let categoricPhenotypesvaluesList = this.state.categoricPhenotypes.map((phenotype, idx) => {
            let deleteButton = <button className="btn btn-danger" onClick={() => this.removePhenotype('categoric', idx)} title="Eliminar valor">-</button>;

            return (
                <div key={"input-value-categoric-div-" + phenotype.id} className="row margin-bottom text-center">
                    <div className="col">
                        {deleteButton}
                    </div>
                    <div className="col">
                        <h5>{phenotype.name}</h5>
                    </div>
                </div>
            );
        });

        valuesComponent = (
            <div className="form-row">
                <div className="col-md-12">
                    <h5>Fenotipos</h5>

                    <p>Numéricos</p>
                    {numericPhenotypesvaluesList}

                    <p>Categóricos</p>
                    {categoricPhenotypesvaluesList}
                </div>
            </div> 
        );

        // Verifico que no este cargando y que el formulario sea valido
        let isValid = this.isFormValid();

        return (
            <div className="modal fade" id={this.modalId} tabIndex="-1" role="dialog" aria-labelledby={this.modalId} aria-hidden="true">
                <div className="modal-dialog" role="document">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title">Agregar patología</h5>
                            <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            <div className="form-row">
                                <div className="col-md-6">
                                    <label htmlFor="pathology-name-input">Nombre de la patología</label>
                                    <input type="text" id="pathology-name-input" name="name" value={this.state.name} className="form-control" onChange={this.handleChange} onKeyPress={this.handleKeyPress}/>
                                </div>
                                <div className="col-md-6 div-autocomplete">
                                    <h5>Nuevo fenotipo</h5>
                                    <Autocomplete
                                        getItemValue={(phenotype) => phenotype.name}
                                        items={this.state.phenotypesSuggeretion}
                                        renderItem={(phenotype, isSelect) =>
                                            <div key={phenotype.id.toString()} className={'autocomplete-option ' + (isSelect ? 'selected' : '')}>
                                                {phenotype.name}
                                            </div>
                                        }
                                        value={this.state.phenotypesInput}
                                        onChange={this.getPhenotypesSuggeretion}
                                        onSelect={(value, phenotype) => { this.selectPhenotype(phenotype) }}
                                    />
                                </div>
                            </div>

                            <div className="row">
                                <div className="col-md-12 text-center margin-top">
                                    <div className="custom-control custom-radio custom-control-inline">
                                        <input className="form-check-input"
                                                type="radio"
                                                name="inlineRadioOptions"
                                                id="numeric"
                                                className="custom-control-input"
                                                checked={this.state.phenotypeType == 'numeric'}
                                                onChange={() => this.setState({ phenotypeType: 'numeric'})}/>
                                        <label className="custom-control-label" htmlFor="numeric">Numéricos</label>
                                    </div>
                                    <div className="custom-control custom-radio custom-control-inline">
                                        <input className="form-check-input"
                                                type="radio"
                                                name="inlineRadioOptions"
                                                id="categoric"
                                                className="custom-control-input"
                                                checked={this.state.phenotypeType == 'categoric'}
                                                onChange={() => this.setState({ phenotypeType: 'categoric' })}/>
                                        <label className="custom-control-label" htmlFor="categoric">Categóricos</label>
                                    </div>
                                </div>
                            </div>
                            
                            <div className="row">
                                <div className="col-md-12">
                                    {valuesComponent}
                                </div>
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-dismiss="modal">Cerrar</button>
                            <button type="button" className="btn btn-primary" disabled={!isValid} onClick={this.savePathology}>Guardar</button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default AddPathologyModal;