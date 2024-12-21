import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ShippingMethod } from 'app/shared/model/enumerations/shipping-method.model';
import { createEntity, getEntity, reset, updateEntity } from './shipping.reducer';

export const ShippingUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const shippingEntity = useAppSelector(state => state.shipping.entity);
  const loading = useAppSelector(state => state.shipping.loading);
  const updating = useAppSelector(state => state.shipping.updating);
  const updateSuccess = useAppSelector(state => state.shipping.updateSuccess);
  const shippingMethodValues = Object.keys(ShippingMethod);

  const handleClose = () => {
    navigate(`/shipping${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.cost !== undefined && typeof values.cost !== 'number') {
      values.cost = Number(values.cost);
    }

    const entity = {
      ...shippingEntity,
      ...values,
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
          method: 'STANDARD',
          ...shippingEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="larissAsstApp.shipping.home.createOrEditLabel" data-cy="ShippingCreateUpdateHeading">
            <Translate contentKey="larissAsstApp.shipping.home.createOrEditLabel">Create or edit a Shipping</Translate>
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
                  id="shipping-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('larissAsstApp.shipping.streetAddress')}
                id="shipping-streetAddress"
                name="streetAddress"
                data-cy="streetAddress"
                type="text"
              />
              <ValidatedField label={translate('larissAsstApp.shipping.city')} id="shipping-city" name="city" data-cy="city" type="text" />
              <ValidatedField
                label={translate('larissAsstApp.shipping.state')}
                id="shipping-state"
                name="state"
                data-cy="state"
                type="text"
              />
              <ValidatedField
                label={translate('larissAsstApp.shipping.postalCode')}
                id="shipping-postalCode"
                name="postalCode"
                data-cy="postalCode"
                type="text"
              />
              <ValidatedField
                label={translate('larissAsstApp.shipping.country')}
                id="shipping-country"
                name="country"
                data-cy="country"
                type="text"
              />
              <ValidatedField
                label={translate('larissAsstApp.shipping.method')}
                id="shipping-method"
                name="method"
                data-cy="method"
                type="select"
              >
                {shippingMethodValues.map(shippingMethod => (
                  <option value={shippingMethod} key={shippingMethod}>
                    {translate(`larissAsstApp.ShippingMethod.${shippingMethod}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label={translate('larissAsstApp.shipping.cost')} id="shipping-cost" name="cost" data-cy="cost" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/shipping" replace color="info">
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

export default ShippingUpdate;
