import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { getEntities as getDescriptions } from 'app/entities/description/description.reducer';
import { getEntities as getColors } from 'app/entities/color/color.reducer';
import { getEntities as getProcessors } from 'app/entities/processor/processor.reducer';
import { getEntities as getMemories } from 'app/entities/memory/memory.reducer';
import { getEntities as getStorages } from 'app/entities/storage/storage.reducer';
import { createEntity, getEntity, reset, updateEntity } from './product-details.reducer';

export const ProductDetailsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const products = useAppSelector(state => state.product.entities);
  const descriptions = useAppSelector(state => state.description.entities);
  const colors = useAppSelector(state => state.color.entities);
  const processors = useAppSelector(state => state.processor.entities);
  const memories = useAppSelector(state => state.memory.entities);
  const storages = useAppSelector(state => state.storage.entities);
  const productDetailsEntity = useAppSelector(state => state.productDetails.entity);
  const loading = useAppSelector(state => state.productDetails.loading);
  const updating = useAppSelector(state => state.productDetails.updating);
  const updateSuccess = useAppSelector(state => state.productDetails.updateSuccess);

  const handleClose = () => {
    navigate(`/product-details${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProducts({}));
    dispatch(getDescriptions({}));
    dispatch(getColors({}));
    dispatch(getProcessors({}));
    dispatch(getMemories({}));
    dispatch(getStorages({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }

    const entity = {
      ...productDetailsEntity,
      ...values,
      product: products.find(it => it.id.toString() === values.product?.toString()),
      description: descriptions.find(it => it.id.toString() === values.description?.toString()),
      color: colors.find(it => it.id.toString() === values.color?.toString()),
      processor: processors.find(it => it.id.toString() === values.processor?.toString()),
      memory: memories.find(it => it.id.toString() === values.memory?.toString()),
      storage: storages.find(it => it.id.toString() === values.storage?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...productDetailsEntity,
          product: productDetailsEntity?.product?.id,
          description: productDetailsEntity?.description?.id,
          color: productDetailsEntity?.color?.id,
          processor: productDetailsEntity?.processor?.id,
          memory: productDetailsEntity?.memory?.id,
          storage: productDetailsEntity?.storage?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="larissAsstApp.productDetails.home.createOrEditLabel" data-cy="ProductDetailsCreateUpdateHeading">
            <Translate contentKey="larissAsstApp.productDetails.home.createOrEditLabel">Create or edit a ProductDetails</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="product-details-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('larissAsstApp.productDetails.name')}
                id="product-details-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('larissAsstApp.productDetails.price')}
                id="product-details-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('larissAsstApp.productDetails.thumbnail')}
                id="product-details-thumbnail"
                name="thumbnail"
                data-cy="thumbnail"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="product-details-product"
                name="product"
                data-cy="product"
                label={translate('larissAsstApp.productDetails.product')}
                type="select"
              >
                <option value="" key="0" />
                {products
                  ? products.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="product-details-description"
                name="description"
                data-cy="description"
                label={translate('larissAsstApp.productDetails.description')}
                type="select"
              >
                <option value="" key="0" />
                {descriptions
                  ? descriptions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="product-details-color"
                name="color"
                data-cy="color"
                label={translate('larissAsstApp.productDetails.color')}
                type="select"
              >
                <option value="" key="0" />
                {colors
                  ? colors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="product-details-processor"
                name="processor"
                data-cy="processor"
                label={translate('larissAsstApp.productDetails.processor')}
                type="select"
              >
                <option value="" key="0" />
                {processors
                  ? processors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="product-details-memory"
                name="memory"
                data-cy="memory"
                label={translate('larissAsstApp.productDetails.memory')}
                type="select"
              >
                <option value="" key="0" />
                {memories
                  ? memories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="product-details-storage"
                name="storage"
                data-cy="storage"
                label={translate('larissAsstApp.productDetails.storage')}
                type="select"
              >
                <option value="" key="0" />
                {storages
                  ? storages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-details" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ProductDetailsUpdate;
